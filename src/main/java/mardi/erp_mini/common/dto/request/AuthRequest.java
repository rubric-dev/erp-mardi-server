package mardi.erp_mini.common.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import mardi.erp_mini.core.entity.auth.Phone;
import mardi.erp_mini.core.entity.auth.VerifyToken;

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
    @Setter
    public static class MetaLogin {
        private String accessToken;
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
    public static class EmailRequest {

        @Email
        @NotNull
        private String email;

        @NotNull
        private VerifyToken.Type type;

        public String getEmail() {
            return email.trim().toLowerCase();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class CreateByEmail {

        @NotBlank
        private String token;

        @NotBlank
        private String name;

        @NotNull
        private String password;


        public String getToken() {
            return token.trim();
        }

        public String getName() {
            return name.trim();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class UpdatePassword {

        @NotBlank
        private String token;

        @NotNull
        private String password;

        public String getToken() {
            return token.trim();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class MetaSignUpRequest {
        private String accessToken;
    }

    @Getter
    @NoArgsConstructor
    public static class SmsSend {

        private String phone;

        public Phone phone() {
            return new Phone(phone);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ValidPhone {
        private String value;
    }
}
