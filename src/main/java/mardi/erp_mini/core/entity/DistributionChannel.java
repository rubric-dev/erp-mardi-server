package mardi.erp_mini.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DistributionChannel {
    DIRECT("DIRECT", "자사몰"),
    ETC("ETC", "기타")
    ;

    private String code;
    private String name;
}
