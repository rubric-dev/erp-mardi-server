package mardi.erp_mini.service;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.dto.request.AuthRequest;
import mardi.erp_mini.dto.response.AuthResponse;
import mardi.erp_mini.entity.auth.*;
import mardi.erp_mini.entity.user.User;
import mardi.erp_mini.entity.user.UserRepository;
import mardi.erp_mini.exception.DuplicateEmailException;
import mardi.erp_mini.exception.NotFoundException;
import mardi.erp_mini.security.JwtTokenProvider;
import mardi.erp_mini.security.enums.RoleType;
import mardi.erp_mini.support.FileUtil;
import mardi.erp_mini.support.aws.AwsSesClient;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthRepository userAuthRepository;
    private final AwsSesClient sesClient;
    private final VerifyTokenRepository verifyTokenRepository;
    private final PasswordEncoder passwordEncoder;
//    private final SmsMessageSender messageSender;
    private final UserRepository userRepository;
    private final FileUtil fileUtil;
    private final RolesRepository rolesRepository;

    @Transactional
    public AuthResponse.Login loginUser(String email, String password) {
        Optional<UserAuth> optionalUserAuth = userAuthRepository.findByEmail(email);

        if (optionalUserAuth.isEmpty()) {
            throw new NotFoundException("not enrolled email");
        }

        UserAuth userAuth = optionalUserAuth.get();
        User user = userRepository.findByAuth(userAuth);

        if (!passwordEncoder.matches(password, userAuth.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // 권한 설정
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(RoleType.USER.getKey()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password, authorities);
        return jwtTokenProvider.generateToken(authentication, user.getId(), email);
    }

    @Transactional
    public AuthResponse.Login refresh(String refreshToken) {
        return jwtTokenProvider.refresh(refreshToken);
    }

    @Transactional
    public void sendSignUpEmail(AuthRequest.EmailRequest dto) {
        RoleType role = dto.getType() == VerifyToken.Type.USER_SIGN_UP ? RoleType.USER : RoleType.ADMIN;

        if (userAuthRepository.existsByEmailAndRole(dto.getEmail(), role)) {
            throw new DuplicateEmailException();
        }

        Optional<VerifyToken> existToken = verifyTokenRepository.findByEmailAndType(dto.getEmail(), dto.getType());

        String tokenValue = "";

        if (existToken.isPresent()) {
            VerifyToken findToken = existToken.get();

            findToken.extendExpiredAt(LocalDateTime.now().plusDays(7));
            tokenValue = findToken.getValue();
        } else {
            VerifyToken verifyToken = VerifyToken.of(dto.getEmail(), dto.getType(), LocalDateTime.now());
            VerifyToken token = verifyTokenRepository.save(verifyToken);

            tokenValue = token.getValue();
        }

        sesClient.sendSignUpEmail(dto.getEmail(), tokenValue);
    }

    @Transactional
    public AuthResponse.Login signUpByEmail(String token, String password, String name, MultipartFile file) {
        VerifyToken verifyToken = getTokenOrThrow(token);

        verifyToken.validateExpired();

        String encPassword = passwordEncoder.encode(password);

        UserAuth initUserAuth = UserAuth.ofUser(verifyToken.getEmail(), encPassword);
        UserAuth userAuth = userAuthRepository.save(initUserAuth);

        String imageUrl = "";

        if (Objects.nonNull(file)) {
            imageUrl = fileUtil.uploadWithRandomName(file);
        }

        User user = User.builder()
                .name(name)
                .imageUrl(imageUrl)
                .email(verifyToken.getEmail())
                .auth(userAuth)
                .build();

        userRepository.save(user);
        verifyTokenRepository.delete(verifyToken);


        // 권한 설정
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(RoleType.USER.getKey()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(verifyToken.getEmail(), password, authorities);
        return jwtTokenProvider.generateToken(authentication, user.getId(), verifyToken.getEmail());
    }

    @Transactional
    public void sendPasswordResetEmail(AuthRequest.EmailRequest dto) {
        Optional<User> user = userRepository.findByEmail(dto.getEmail());

        if (user.isEmpty()) throw new NotFoundException("user not found");

        Optional<VerifyToken> existToken = verifyTokenRepository.findByEmailAndType(dto.getEmail(), dto.getType());

        String tokenValue = "";

        if (existToken.isPresent()) {
            VerifyToken findToken = existToken.get();

            findToken.extendExpiredAt(LocalDateTime.now().plusDays(7));
            tokenValue = findToken.getValue();
        } else {
            VerifyToken verifyToken = VerifyToken.ofPasswordReset(dto.getEmail(), LocalDateTime.now());
            VerifyToken token = verifyTokenRepository.save(verifyToken);

            tokenValue = token.getValue();
        }

        sesClient.sendPasswordResetEmail(dto.getEmail(), user.get().getName(), tokenValue);
    }

    @Transactional
    public void initPassword(String token, String password) {
        VerifyToken verifyToken = getTokenOrThrow(token);
        verifyToken.validateExpired();

        String encPassword = passwordEncoder.encode(password);
        Optional<UserAuth> userAuth = Optional.empty();

        if (verifyToken.getType() == VerifyToken.Type.USER_PASSWORD_RESET) {
            userAuth = userAuthRepository.findByEmailAndRole(verifyToken.getEmail(), RoleType.USER);
        } else userAuthRepository.findByEmailAndRole(verifyToken.getEmail(), RoleType.ADMIN);

        if (userAuth.isEmpty()) throw new NotFoundException("auth not found");

        userAuth.get().modifyPassword(encPassword);

        verifyTokenRepository.delete(verifyToken);
    }

    private VerifyToken getTokenOrThrow(String token) {
        return verifyTokenRepository.findByValue(token).orElseThrow(() -> new NotFoundException("token not found"));
    }

    @Transactional
    public void sendValidatePhoneMessage(Phone phone) {
        VerifyToken verifyToken = VerifyToken.ofValidPhone(phone, LocalDateTime.now());
        verifyTokenRepository.save(verifyToken);

//        messageSender.sendValidatePhoneMessage(phone.getStrippedValue(), verifyToken.getValue());
    }

    @Transactional
    public void verifyPhoneBySmsValue(String value) {
        Optional<VerifyToken> token = verifyTokenRepository.findByValue(value);

        if (token.isEmpty()) throw new NotFoundException("token not found");
        verifyTokenRepository.delete(token.get());
    }
}
