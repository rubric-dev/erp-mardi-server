package mardi.erp_mini.core.entity.product;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.EnumPath;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeasonCode {
    SPRING("1", "SPRING"),
    SUMMER("2", "SUMMER"),
    FALL("3", "FALL"),
    WINTER("4", "WINTER")
    ;

    private final String code;
    private final String name;

    public static Expression<?> returnName(EnumPath<SeasonCode> seasonCode) {
        return new CaseBuilder()
                .when(seasonCode.eq(SeasonCode.SPRING)).then(SeasonCode.SPRING.getName())
                .when(seasonCode.eq(SeasonCode.SUMMER)).then(SeasonCode.SUMMER.getName())
                .when(seasonCode.eq(SeasonCode.FALL)).then(SeasonCode.FALL.getName())
                .when(seasonCode.eq(SeasonCode.WINTER)).then(SeasonCode.WINTER.getName())
                .otherwise("");
    }
}
