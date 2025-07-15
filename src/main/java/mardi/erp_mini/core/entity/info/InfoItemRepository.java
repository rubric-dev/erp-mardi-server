package mardi.erp_mini.core.entity.info;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfoItemRepository extends JpaRepository<InfoItem, Long> {
    List<InfoItem> findAllByOrderByCodeAsc();
}
