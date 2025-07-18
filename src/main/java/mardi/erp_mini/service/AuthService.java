package mardi.erp_mini.service;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.request.AuthRequest;
import mardi.erp_mini.common.dto.response.AuthResponse;
import mardi.erp_mini.core.entity.auth.UserAuth;
import mardi.erp_mini.core.entity.auth.UserAuthRepository;
import mardi.erp_mini.core.entity.brand.BrandUser;
import mardi.erp_mini.core.entity.brand.BrandUserRepository;
import mardi.erp_mini.core.entity.user.User;
import mardi.erp_mini.core.entity.user.UserCustomRepository;
import mardi.erp_mini.exception.NotFoundException;
import mardi.erp_mini.security.JwtTokenProvider;
import mardi.erp_mini.security.enums.RoleType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCustomRepository userCustomRepository;
    private final BrandUserRepository brandUserRepository;

    @Transactional
    public void createUser(AuthRequest.Create request) {

        UserAuth userAuth = userAuthRepository.save(UserAuth.ofUser(request.getEmail(), initPassword()));
        //TODO: 중복 방지 로직 추가
        User user = userCustomRepository.createUser(
                request.getName()
                , request.getEmail()
                , userAuth
        );

        brandUserRepository.saveAll(request.getBrandIds().stream()
                .map(brandId -> BrandUser.builder()
                        .brandId(brandId)
                        .userId(user.getId())
                        .build()
                )
                .toList());
    }

    @Transactional
    public AuthResponse.Login loginUser(String email, String password) {
        UserAuth userAuth = userAuthRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("no user found with email: " + email));

        if (!passwordEncoder.matches(password, userAuth.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // 권한 설정
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(RoleType.USER.getKey()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password, authorities);
        return jwtTokenProvider.generateToken(authentication, userAuth.getId(), email);
    }

    @Transactional
    public AuthResponse.Login refresh(String refreshToken) {
        return jwtTokenProvider.refresh(refreshToken);
    }

    public String initPassword() {
//        String password = UUID.randomUUID().toString().replaceAll("-", "");
        String password = "1234";
        return passwordEncoder.encode(password);
    }
}
