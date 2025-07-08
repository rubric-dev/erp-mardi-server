package mardi.erp_mini.core.entity.user;

import java.util.List;

public interface UserCustomRepository {
    User findOneById(Long userId);

    User createUser(String name, String email);

    List<User> findAll();

    void deleteUser(Long userId);
}
