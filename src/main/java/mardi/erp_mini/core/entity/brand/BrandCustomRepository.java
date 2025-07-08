package mardi.erp_mini.core.entity.brand;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandCustomRepository {
    List<Brand> findByIds(List<Long> brandIds);
}
