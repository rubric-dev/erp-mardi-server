package mardi.erp_mini.core.entity.user;

import mardi.erp_mini.core.entity.auth.UserAuth;
import mardi.erp_mini.core.response.UserResponse.BrandLineDetail;

public interface UserCustomRepository {

    User createUser(String name, String username, String email, UserAuth userAuth);

    void deleteUser(Long userId);

    BrandLineDetail findFirstByUserIdOrderBySeq(Long userId);
}
