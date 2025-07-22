package mardi.erp_mini.common.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AuthRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Login {

        @NotNull
        private String username;

        @NotNull
        private String password;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Refresh {

        @NotBlank
        private String refreshToken;

        public String getRefreshToken() {
            return refreshToken.trim();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Create {
        @NotNull
        private String name;

        @NotNull
        private String username;

        @Email
        @NotNull
        private String email;

        @NotNull
        private List<String> brandLineCodes;

        public String getEmail() {
            return email.trim().toLowerCase();
        }
    }
}
