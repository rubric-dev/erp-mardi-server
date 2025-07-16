package mardi.erp_mini.core.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WareHouseRepository extends JpaRepository<WareHouse, Long> {
    List<WareHouse> findByBrandLineCodeOrderByName(String brandLineCode);
}
