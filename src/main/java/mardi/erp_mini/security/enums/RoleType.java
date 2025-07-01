package mardi.erp_mini.security.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN("ADMIN", "어드민"),
    USER("USER", "유저");

    private final String key;

    private final String title;

    RoleType(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
