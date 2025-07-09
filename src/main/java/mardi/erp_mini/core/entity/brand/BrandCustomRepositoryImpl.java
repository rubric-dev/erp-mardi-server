package mardi.erp_mini.core.entity.brand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BrandCustomRepositoryImpl implements BrandCustomRepository {

    private final BrandRepository brandRepository;

    //TODO: 브랜드가 없는 경우 어떻게 표출할지 여부에 따라 수정
    @Override
    public List<Brand> findByIds(List<Long> brandIds) {
        return brandRepository.findAllById(brandIds);
    }
}
