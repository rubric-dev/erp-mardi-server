package mardi.erp_mini.service;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ReorderRequest;
import mardi.erp_mini.core.entity.product.ProductColorSize;
import mardi.erp_mini.core.entity.product.ProductColorSizeRepository;
import mardi.erp_mini.core.entity.reorder.Reorder;
import mardi.erp_mini.core.entity.reorder.ReorderDslRepository;
import mardi.erp_mini.core.entity.reorder.ReorderRepository;
import mardi.erp_mini.core.response.ReorderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReorderService {
    private final ReorderRepository reorderRepository;
    private final ProductColorSizeRepository productColorSizeRepository;
    private final ReorderDslRepository reorderDslRepository;

    @Transactional
    public Long post(ReorderRequest.Create dto){
        ProductColorSize pcs = productColorSizeRepository.findOneById(dto.getProductColorSizeId());

        Reorder reorder = Reorder.builder()
                .brandLine(pcs.getBrandLine())
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

    public List<ReorderResponse.ListRes> getReorderList(ReorderRequest.SearchParam searchParam) {
        return reorderDslRepository.searchList(searchParam);
    }
}
