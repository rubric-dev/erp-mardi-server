package mardi.erp_mini.service;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ReorderRequest;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.entity.brand.BrandUserRepository;
import mardi.erp_mini.core.entity.option.DepletionDslRepository;
import mardi.erp_mini.core.entity.product.ProductColorSize;
import mardi.erp_mini.core.entity.product.ProductColorSizeRepository;
import mardi.erp_mini.core.entity.product.SeasonCode;
import mardi.erp_mini.core.entity.reorder.Reorder;
import mardi.erp_mini.core.entity.reorder.ReorderDslRepository;
import mardi.erp_mini.core.entity.reorder.ReorderRepository;
import mardi.erp_mini.core.entity.reorder.ReorderSearch;
import mardi.erp_mini.core.response.DepletionResponse;
import mardi.erp_mini.core.response.ReorderResponse;
import mardi.erp_mini.security.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReorderService {
    private final ReorderRepository reorderRepository;
    private final ProductColorSizeRepository productColorSizeRepository;
    private final ReorderDslRepository reorderDslRepository;
    private final DepletionDslRepository depletionDslRepository;
    private final BrandUserRepository brandUserRepository;

    @Transactional
    public Long post(ReorderRequest.Create dto){
        ProductColorSize pcs = productColorSizeRepository.findOneById(dto.getProductColorSizeId());

        Reorder reorder = Reorder.builder()
                .brandLine(pcs.getBrandLine())
                .productCode(pcs.getProductCode())
                .colorCode(pcs.getInfoColor().getCode())
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

    @Transactional(readOnly = true)
    public List<ReorderResponse.ListRes> getReorderList(ReorderRequest.SearchParam searchParam) {

        if(searchParam.getBrandLineCode() == null){
            searchParam.setBrandLineCode(brandUserRepository.findMainBrandByUserId(AuthUtil.getUserId()).getBrandLineCode());
        }

        if(searchParam.getSeasonCode() == null){
            searchParam.setYear(LocalDate.now().getYear());
            searchParam.setSeasonCode(SeasonCode.recentSeasonCode());
        }

        if(searchParam.getSearchDate() == null||searchParam.getSearchDate().getFrom() == null || searchParam.getSearchDate().getTo() == null){
            searchParam.setSearchDate(new ReorderRequest.DateContainer(
                    LocalDate.now().minusDays(15),
                    LocalDate.now()
            ));
        }

        List<ReorderResponse.Product> products = reorderDslRepository.getReorderList(
                searchParam.getBrandLineCode(),
                searchParam.getYear(),
                searchParam.getSeasonCode(),
                searchParam.getItemCodes(),
                searchParam.getGraphicCodes(),
                searchParam.getProductCodes()
        );

        List<Long> productColorSizeIds = products.stream()
                .map(ReorderResponse.Product::getProductColorSizeId)
                .toList();

        List<ReorderSearch> results =  reorderDslRepository.getReorderStats(
                productColorSizeIds,
                searchParam.getGraphicCodes(),
                searchParam.getSearchDate().getTo(),
                searchParam.getSearchDate().getFrom(),
                searchParam.getWareHouseId(),
                searchParam.getDistChannel()
        );

        List<DepletionResponse.ListRes> depletionLevels = depletionDslRepository.getActiveDepletionLevels(searchParam.getBrandLineCode());

        List<ReorderResponse.User> users = reorderRepository.findLatestReorderUser(productColorSizeIds, searchParam.getGraphicCodes());

        List<ReorderResponse.ListRes> combinedList = products.stream()
                .map(product -> {
                    ReorderSearch result = results.stream()
                        .filter(r -> 
                            product.getProductColorSizeId().equals(r.getProductColorSizeId()) &&
                            product.getGraphicCode().equals(r.getGraphicCode())
                        )
                        .findFirst()
                        .orElse(null);
                    UserByResponse reorderBy = null;
                    if (result != null) {
                        reorderBy = users.stream()
                            .filter(user ->
                                user.getProductColorSizeId().equals(result.getProductColorSizeId()) &&
                                user.getGraphicCode().equals(result.getGraphicCode())
                            )
                            .findFirst()
                            .map(user -> UserByResponse.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .imageUrl(user.getImageUrl())
                                .build()
                            )
                            .orElse(null);
                    }
                    if (result != null) {
                        return ReorderResponse.ListRes.builder()
                            .productColorSizeId(product.getProductColorSizeId())
                            .productImageUrl(product.getProductImageUrl())
                            .productCode(product.getProductCode())
                            .productName(product.getProductName())
                            .color(product.getColorCode())
                            .size(product.getSizeName())
                            .graphic(product.getGraphicCode())
                            .moqQty(product.getMoqQty())
                            .leadTime(product.getLeadTime())
                            .availableOpenQty(result.getAvailableOpenQty())
                            .expectedInboundQty(result.getExpectedInboundQty())
                            .periodInboundQty(result.getPeriodInboundQty())
                            .dailyAvgSalesQty(result.getDailyAvgSalesQty())
                            .periodSalesQty(result.getPeriodSalesQty())
                            .accExpectedOutboundQty(result.getAccExpectedOutboundQty())
                            .availableEndQty(result.getAvailableEndQty())
                            .salesQty(result.getSalesQty())
                            .depletionRate(result.getDepletionRate())
                            .depletionRatePlan(result.getDepletionRatePlan())
                            .depletionLevel(determineDepletionLevel(result.getDepletionRate(), depletionLevels))
                            .sellableDays(result.getSellableDays())
                            .sellableQty(result.getSellableQty())
                            .reorderBy(reorderBy)
                            .reorderAt(result.getReorderAt())
                            .build();
                    }
                    return null;
                })
                .filter(res -> res != null)
                .collect(Collectors.toList());
        return combinedList;
    }

    private String determineDepletionLevel(int depletionRate, List<DepletionResponse.ListRes> depletionLevels) {
        return depletionLevels.stream()
                .filter(level -> depletionRate >= level.getGreaterThan() && depletionRate <= level.getLesserThan())
                .findFirst()
                .map(DepletionResponse.ListRes::getName)
                .orElse(null);
    }

}