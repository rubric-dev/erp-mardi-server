package mardi.erp_mini.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private Long id;
        private String name;
        private String username;
        private String email;
        private String imageUrl;
        private boolean isAdmin;
        private List<BrandLineDetail> brands;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListRes {
        private Long id;
        private String name;
        private String username;
        private String email;
        private String imageUrl;
        private BrandLineDetail brandLine;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BrandLineDetail {
        private Long id;
        private String code;
        private String name;
    }
}
