package mardi.erp_mini.core.entity.brand;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandUserRepository extends JpaRepository<BrandUser, Long> {
    List<Long> findBrandIdByUserIdAndIsDeletedIsFalse(Long userId);
}
