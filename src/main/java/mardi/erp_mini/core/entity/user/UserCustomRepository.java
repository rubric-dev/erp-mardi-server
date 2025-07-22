package mardi.erp_mini.core.entity.user;

import mardi.erp_mini.core.entity.auth.UserAuth;

import java.util.List;

public interface UserCustomRepository {

    User createUser(String name, String username, String email, UserAuth userAuth);

    List<User> findAll();

    void deleteUser(Long userId);
}
