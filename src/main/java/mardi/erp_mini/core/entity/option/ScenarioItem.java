package mardi.erp_mini.core.entity.option;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.BaseEntity;
import mardi.erp_mini.core.entity.info.InfoItem;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ScenarioItem extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("소진율 시나리오")
  @JoinColumn(name = "scenario_id", referencedColumnName = "id")
  @ManyToOne(fetch = FetchType.LAZY)
  Scenario scenario;

  @Comment("카테고리")
  @JoinColumn(name = "item_cd", referencedColumnName = "code")
  @ManyToOne(fetch = FetchType.LAZY)
  InfoItem infoItem;

  @Comment("소진율 단계")
  @JoinColumn(name = "depletion_lv_id", referencedColumnName = "id")
  @ManyToOne(fetch = FetchType.LAZY)
  DepletionLevel depletionLevel;

  @Comment("소진율 단계 기준 이상 값")
  private int greaterThan;

  @Comment("소진율 단계 기준 이하 값")
  private int lesserThan;

  public void updateParams(int greaterThan, int lesserThan) {
    this.greaterThan = greaterThan;
    this.lesserThan = lesserThan;
  }
}
