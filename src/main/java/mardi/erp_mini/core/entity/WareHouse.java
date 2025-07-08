package mardi.erp_mini.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;


//판매
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WareHouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //창고안에 있는 상품들이 브랜드별로 나뉘는데, 적재되는 곳이 똑같기 때문에 같은 창고라도 브랜드 별로 로우 나뉠 확률이 높음
    private Long brandId;
    private String name;
    @Embedded
    private Address address;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

