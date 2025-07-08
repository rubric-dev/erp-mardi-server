package mardi.erp_mini.common.dto.request;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AgGridRequest {
    private int startRow;
    private int endRow;
    private String keyword;
    private List<SortModel> sortModel;
    private Map<String, FilterModel> filterModel = new HashMap<>();

    @Data
    public static class SortModel {
        private String colId; // 예: channelName
        private String sort;  // asc or desc
    }

    @Data
    public static class FilterModel {
        private String filterType; // "number", "text", etc

        // 단일 조건 필터일 경우 사용
        private String type;       // "equals", "greaterThan", etc
        private String filter;

        // 복합 조건 필터일 경우 사용
        private String operator;   // "AND" or "OR"
        private List<FilterCondition> conditions;

        // ✅ Set Filter용 필드 추가
        private List<String> values;

        @Data
        public static class FilterCondition {
            private String filterType; // 중첩된 조건도 filterType이 포함됨
            private String type;
            private String filter;
        }

        public boolean isCompound() {
            return conditions != null && !conditions.isEmpty();
        }
    }

}
