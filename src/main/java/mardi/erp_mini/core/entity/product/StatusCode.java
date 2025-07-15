package mardi.erp_mini.core.entity.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {
    CURRENT("CURRENT", "정상"),
    CARRYOVER("CARRYOVER", "이월")
    ;

    private final String code;
    private final String name;

}
