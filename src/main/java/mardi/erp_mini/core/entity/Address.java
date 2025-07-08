package mardi.erp_mini.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Address {

    @Column(name = "address")
    private String address;       // 기본 주소 (도로명, 지번 등)

    @Column(name = "detail_address")
    private String detailAddress; // 상세 주소 (동호수, 건물 이름 등)

    @Column(name = "zip_code")
    private String zipCode;       // 우편번호

    // TODO: Validation 추가
    public Address(String address, String detailAddress, String zipCode) {
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipCode = zipCode;
    }
}
