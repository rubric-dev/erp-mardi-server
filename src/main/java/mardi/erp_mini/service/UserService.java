package mardi.erp_mini.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.auth.UserAuth;
import mardi.erp_mini.core.entity.auth.UserAuthRepository;
import mardi.erp_mini.core.entity.brand.BrandLineRepository;
import mardi.erp_mini.core.entity.brand.BrandUser;
import mardi.erp_mini.core.entity.brand.BrandUserRepository;
import mardi.erp_mini.core.entity.user.User;
import mardi.erp_mini.core.entity.user.UserCustomRepository;
import mardi.erp_mini.core.entity.user.UserRepository;
import mardi.erp_mini.core.response.UserResponse;
import mardi.erp_mini.security.AuthUtil;
import mardi.erp_mini.security.enums.RoleType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserCustomRepository userCustomRepository;
    private final BrandLineRepository brandLineRepository;
    private final BrandUserRepository brandUserRepository;
    private final UserAuthRepository userAuthRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getAuthenticatedUser() {
        Long userId = AuthUtil.getUserId();
        return userRepository.findOneById(userId);
    }

    @Transactional(readOnly = true)
    public UserResponse.Detail getUserById(Long userId) {
        User user = userRepository.findOneById(userId);
        UserAuth userAuth = userAuthRepository.findOneByUsername(user.getUsername());

        List<String> brandLineCodes = brandUserRepository.findAllByUserId(userId)
                .stream()
                .map(BrandUser::getBrandLineCode)
                .toList();

        return UserResponse.Detail.builder()
                .id(user.getId())
                .username(userAuth.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .brands(getBrandLineDetails(brandLineCodes))
                .isAdmin(userAuth.getRole() == RoleType.ADMIN)
                .build();
    }

    @Transactional(readOnly = true)
    public List<UserResponse.ListRes> getUserList() {
        return userCustomRepository.findAll().stream()
                .map(user -> UserResponse.ListRes.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .imageUrl(user.getImageUrl())
                        .brandLine(
                            userCustomRepository.findFirstByUserIdOrderBySeq(user.getId())
                        )
                        .build())
                .toList();
    }

    private List<UserResponse.BrandLineDetail> getBrandLineDetails(List<String> brandLineCodes){
        return brandLineRepository.findAllByCodeInOrderByCode(brandLineCodes)
                .stream()
                .map(brand -> UserResponse.BrandLineDetail.builder()
                                .id(brand.getId())
                                .name(brand.getName())
                                .build()
                )
                .toList();
    }

    public void deleteUser(Long userId) {
        userCustomRepository.deleteUser(userId);

    }
}