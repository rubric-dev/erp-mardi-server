package mardi.erp_mini.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthResponse {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Login {
        private String accessToken;
        private String refreshToken;
        private String type;
        private String email;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MetaLogin {
        private String accessToken;
        private String refreshToken;
        private String type;
        private boolean isMember;

        public static MetaLogin ofFailResponse(){
            return MetaLogin.builder()
                    //명시적 표현
                    .isMember(false)
                    .build();
        }
    }
}
