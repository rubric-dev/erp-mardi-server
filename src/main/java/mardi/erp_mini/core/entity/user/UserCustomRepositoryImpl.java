package mardi.erp_mini.core.entity.user;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.auth.UserAuth;
import mardi.erp_mini.core.entity.auth.UserAuthRepository;
import mardi.erp_mini.core.entity.brand.BrandLine;
import mardi.erp_mini.core.entity.brand.BrandLineRepository;
import mardi.erp_mini.core.entity.brand.BrandUser;
import mardi.erp_mini.core.entity.brand.BrandUserRepository;
import mardi.erp_mini.core.response.UserResponse.BrandLineDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final BrandLineRepository brandLineRepository;
    private final BrandUserRepository brandUserRepository;


    @Override
    public User createUser(String name, String username, String email, UserAuth userAuth) {
        return userRepository.save(User.builder()
                .name(name)
                .username(username)
                .email(email)
                .auth(userAuth)
                .build()
        );
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findOneById(userId);
        UserAuth userAuth = userAuthRepository.findOneById(user.getAuth().getId());
        List<BrandUser> brandUsers = brandUserRepository.findAllByUserId(userId);
        user.delete();
        userAuth.delete();
        brandUsers.forEach(BrandUser::delete);
    }

    @Override
    public BrandLineDetail findFirstByUserIdOrderBySeq(Long userId) {
        BrandUser brandUser = brandUserRepository.findMainBrandByUserId(userId);
        BrandLine brandLine = brandLineRepository.findOneByCode(brandUser.getBrandLineCode());

        return BrandLineDetail.builder()
            .id(brandLine.getId())
            .code(brandLine.getCode())
            .name(brandLine.getName())
            .build();
    }
}
