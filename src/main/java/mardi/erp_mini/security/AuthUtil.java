package mardi.erp_mini.security;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.auth.UserAuth;
import mardi.erp_mini.core.entity.auth.UserAuthRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final UserAuthRepository userAuthRepository;

    //TODO: null인 경
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
            return null;
//            throw new AuthenticationException("Invalid or expired authentication token") {
//            };
        }
    }

    public static Long getUserId(){
        return getSessionUser().getUserId();
    }

}
