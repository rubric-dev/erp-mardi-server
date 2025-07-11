package mardi.erp_mini.service;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ReorderRequest;
import mardi.erp_mini.core.entity.brand.BrandLine;
import mardi.erp_mini.core.entity.brand.BrandLineRepository;
import mardi.erp_mini.core.entity.product.ProductColorSize;
import mardi.erp_mini.core.entity.product.ProductColorSizeRepository;
import mardi.erp_mini.core.entity.reorder.Reorder;
import mardi.erp_mini.core.entity.reorder.ReorderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReorderService {
    private final ReorderRepository reorderRepository;
    private final ProductColorSizeRepository productColorSizeRepository;
    private final BrandLineRepository BrandLineRepository;

    @Transactional
    public Long post(Long sessionUserId, ReorderRequest.Create dto){
        ProductColorSize pcs = productColorSizeRepository.findOneById(dto.getProductColorSizeId());
        BrandLine brandLine = BrandLineRepository.findOneById(dto.getBrandId());

        Reorder reorder = Reorder.builder()
                .brandLine(brandLine)
                .productCode(pcs.getProductCode())
                .colorCode(pcs.getColorCode())
                .infoSize(pcs.getInfoSize())
                .quantity(dto.getQuantity())
                .build();

        return reorderRepository.save(reorder).getId();
    }

    @Transactional
    public void confirm(Long sessionUserId, Long id) {
        Reorder reorder = reorderRepository.findOneById(id);
        reorder.confirm(sessionUserId);
    }
}
