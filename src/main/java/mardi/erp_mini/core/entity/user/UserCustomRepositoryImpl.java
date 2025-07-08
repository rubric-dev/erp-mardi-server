package mardi.erp_mini.core.entity.user;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final UserRepository userRepository;

    @Override
    public User findOneById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found. id : " + userId));
    }

    @Override
    public User createUser(String name, String email) {
        return userRepository.save(User.builder()
                        .name(name)
                        .email(email)
                .build());
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long userId) {
        User user = findOneById(userId);
        user.delete();
    }
}
