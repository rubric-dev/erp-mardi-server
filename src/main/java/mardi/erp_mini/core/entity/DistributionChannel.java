package mardi.erp_mini.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DistributionChannel {
    DIRECT(1, "DIRECT", "자사몰"),
    ETC(2, "ETC", "기타")
    ;

    private int seq;
    private String code;
    private String name;
}
