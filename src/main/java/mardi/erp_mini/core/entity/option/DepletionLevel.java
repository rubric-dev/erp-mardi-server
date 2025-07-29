package mardi.erp_mini.core.entity.option;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import mardi.erp_mini.common.BaseEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DepletionLevel extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("순서")
  @ColumnDefault("0")
  private int seq;

  @Comment("소진율 단계 이름")
  private String name;

  @Comment("소진율 단계 기준 이상 기본값")
  private int greaterThan;

  @Comment("소진율 단계 기준 이하 기본값")
  private int lesserThan;

}
