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
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DepletionDslRepository {

  private final JPAQueryFactory queryFactory;
  private final ScenarioRepository scenarioRepository;

  public List<DepletionResponse.ListRes> getDepeletionLevels(Long scenarioId, String itemCode) {
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
            .and(itemCode != null ? scenarioItem.infoItem.code.eq(itemCode) : null))
        .orderBy(scenarioItem.greaterThan.asc())
    .fetch();
  }

  public List<DepletionResponse.ListRes> getActiveDepletionLevels(String brandLineCode) {
      Long scenarioId = scenarioRepository.findByBrandLineCodeAndIsActive(brandLineCode, true).getId();
      return getDepeletionLevels(scenarioId, null);
  }

  public ScenarioItem getScenarioItem(Long scenarioId, String itemCode, Long depletionLevelId) {
      return queryFactory.selectFrom(scenarioItem)
              .where(scenarioItem.scenario.id.eq(scenarioId)
               .and(scenarioItem.infoItem.code.eq(itemCode))
               .and(scenarioItem.depletionLevel.id.eq(depletionLevelId)))
              .fetchOne();
  }
}
