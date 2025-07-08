package mardi.erp_mini.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserRequest {

    @Getter
    @NoArgsConstructor
    public static class Update {
        private Long companyId;
        //직급
        private Long rankId;
        //직무
        private Long jobId;
        //직책
        private Long positionId;
        private boolean isManager;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateBulk {
        private Long companyId;
        private List<UserUpdate> users = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    public static class UserUpdate {
        private String email;
        //직급
        private String rank;
        //직무
        private String job;
        //직책
        private String position;
        private String role;
    }
}
