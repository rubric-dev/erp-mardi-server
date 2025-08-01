package mardi.erp_mini.core.entity.product;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.EnumPath;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public enum SeasonCode {
    SPRING(1,"SPRING", "SPRING"),
    SUMMER(2, "SUMMER", "SUMMER"),
    FALL(3,"FALL", "FALL"),
    WINTER(4,"WINTER", "WINTER")
    ;

    private final int seq;
    private final String name;
    private final String code;

    public static SeasonCode recentSeasonCode(){
        int month = LocalDate.now().getMonthValue();
        if (month < 4) return SPRING;
        if (month < 7) return SUMMER;
        if (month < 9) return FALL;
        return WINTER;
    }

    public static Expression<?> returnName(EnumPath<SeasonCode> seasonCode) {
        return new CaseBuilder()
                .when(seasonCode.eq(SeasonCode.SPRING)).then(SeasonCode.SPRING.getName())
                .when(seasonCode.eq(SeasonCode.SUMMER)).then(SeasonCode.SUMMER.getName())
                .when(seasonCode.eq(SeasonCode.FALL)).then(SeasonCode.FALL.getName())
                .when(seasonCode.eq(SeasonCode.WINTER)).then(SeasonCode.WINTER.getName())
                .otherwise("");
    }
}
