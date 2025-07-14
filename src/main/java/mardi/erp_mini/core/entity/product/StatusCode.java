package mardi.erp_mini.core.entity.product;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatusCode {
    CURRENT("CURRENT", "정상"),
    CARRYOVER("CARRYOVER", "이월")
    ;

    private String code;
    private String name;

}
