package mardi.erp_mini.core.entity.brand;

import java.util.List;

public interface BrandListCustomRepository {
    List<BrandLine> findByIds(List<Long> brandIds);
}
