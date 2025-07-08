package mardi.erp_mini.common.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class AuthRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Login {

        @Email
        @NotNull
        private String email;

        @NotNull
        private String password;

        public String getEmail() {
            return email.trim().toLowerCase();
        }
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

        private String name;

        @Email
        @NotNull
        private String email;

        @NotNull
        private List<Long> brandIds;

        public String getEmail() {
            return email.trim().toLowerCase();
        }
    }
}
