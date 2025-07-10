package mardi.erp_mini.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ScenarioRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Create {
        private String name;
    }
}
