package mardi.erp_mini.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserResponse {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Detail {
        private Long id;
        private String name;
        private String email;
        private String imageUrl;
        @JsonProperty("isSystemAdmin")
        private boolean isSystemAdmin;
        private List<CompanyContainer> companyList = new ArrayList<>();
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ListRes {
        private Long id;
        private String name;
        private String email;
        private String imageUrl;
        private String role;
        private boolean isOwner;
        private RankContainer rank;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class RankContainer {
        private Long id;
        private String name;
    }



    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class InviteTokenResponse {
        private Long id;
        private String email;
        private CompanyDetail company;
        private boolean isSignedUp;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CompanyDetail {
        private Long id;
        private String name;
        private String imageUrl;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CompanyContainer {
        private Long id;
        private String name;
        private String role;
        private RankContainer rank;
        private boolean isLeader;
    }

}
