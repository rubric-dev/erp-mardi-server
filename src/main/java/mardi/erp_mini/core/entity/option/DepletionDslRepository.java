package mardi.erp_mini.core.entity.option;

import static mardi.erp_mini.core.entity.option.QDepletionLevel.depletionLevel;
import static mardi.erp_mini.core.entity.option.QScenarioItem.scenarioItem;
import static mardi.erp_mini.core.entity.user.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.response.DepletionResponse;
import mardi.erp_mini.core.response.DepletionResponse.ListRes;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DepletionDslRepository {

  private final JPAQueryFactory queryFactory;

  public List<ListRes> getDepeletionLevels(Long scenarioId, Long categoryId) {
    return queryFactory.select(
        Projections.constructor(
            DepletionResponse.ListRes.class,
            scenarioItem.depletionLevel.id,
            depletionLevel.name,
            scenarioItem.greaterThan,
            scenarioItem.lesserThan,
            Projections.constructor(
                UserByResponse.class,
                user.id,
                user.name,
                user.imageUrl
            ),
            scenarioItem.updatedAt
        )
    ).from(scenarioItem)
        .join(depletionLevel)
            .on(depletionLevel.id.eq(scenarioItem.depletionLevel.id))
            .fetchJoin()
        .leftJoin(user).on(user.id.eq(scenarioItem.modifiedBy))
        .where(scenarioItem.scenario.id.eq(scenarioId)
            .and(scenarioItem.infoItem.id.eq(categoryId)))
        .orderBy(scenarioItem.greaterThan.asc())
    .fetch();
  }
}
