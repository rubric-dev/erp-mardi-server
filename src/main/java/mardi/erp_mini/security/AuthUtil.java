package mardi.erp_mini.security;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.entity.auth.UserAuth;
import mardi.erp_mini.entity.auth.UserAuthRepository;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final UserAuthRepository userAuthRepository;

    public static UserAuth getAuthInfo (){
        getSessionUser().getAuthorities();

        return null;
    }

    public static CustomUserDetails getSessionUser() {
        try {
            return (CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
        } catch (Exception e) {
            throw new AuthenticationException("Invalid or expired authentication token") {
            };
        }
    }

    public static Long getUserId(){
        return getSessionUser().getUserId();
    }

}
