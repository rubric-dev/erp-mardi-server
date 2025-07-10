package mardi.erp_mini.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


//판매
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesOrder extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    private String orderNo;
    private LocalDate orderDate;

    private String salesType;             // 내수/수출 구분
    private String paymentMethod;         // 결제 방식 (카드/현금/외상 등)
    private String status;                // 상태 (주문접수, 출고완료 등)
    private String note;                  // 비고
}

