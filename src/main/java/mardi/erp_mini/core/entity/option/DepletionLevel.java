package mardi.erp_mini.core.entity.option;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import mardi.erp_mini.common.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DepletionLevel extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ColumnDefault("0")
  private int seq;
  private String name;
}
