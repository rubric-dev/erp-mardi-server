package mardi.erp_mini.core.entity.auth;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.StringUtils;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Phone {

    @Column(name = "phone")
    @Comment("전화번호")
    private String value;

    public Phone(@Nullable String value) {
        if (!StringUtils.hasText(value)) {
            this.value = null;
            return;
        }

        String trimmedValue = value.trim();

        final String domesticPhoneNumberRegex = "^0\\d{9,10}$";
        if (trimmedValue.matches(domesticPhoneNumberRegex)) {
            this.value = trimmedValue;
            return;
        }

        final String internationalPhoneNumberRegex = "^\\+?[1-9]\\d{7,14}$";
        if (trimmedValue.matches(internationalPhoneNumberRegex)) {
            this.value = trimmedValue;
            return;
        }

        throw new IllegalArgumentException("유효하지 않는 전화번호입니다(" + trimmedValue + ")");
    }

    public boolean isValid() {
        return StringUtils.hasText(this.value);
    }

    public String get() {
        return getStrippedValue();
    }

    public String getStrippedValue() {
        return this.value;
    }

    public String toString() {
        return "phone='" + get() + '\'';
    }
}
