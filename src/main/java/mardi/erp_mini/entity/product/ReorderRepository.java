package mardi.erp_mini.entity.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReorderRepository extends JpaRepository<Reorder, Long> {
}
