package mardi.erp_mini.support;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.user.UserRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RoleValidator {
    private final UserRepository userRepository;
}
