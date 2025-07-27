package mardi.erp_mini.service;


import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ReorderRequest;
import mardi.erp_mini.api.request.ReorderRequest.Create;
import mardi.erp_mini.api.request.ReorderRequest.DateContainer;
import mardi.erp_mini.api.request.ReorderRequest.SearchParam;
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
import mardi.erp_mini.core.response.ReorderResponse.ListRes;
import mardi.erp_mini.core.response.ReorderResponse.Product;
import mardi.erp_mini.core.response.ReorderResponse.ReorderListRes;
import mardi.erp_mini.core.response.ReorderResponse.User;
import mardi.erp_mini.security.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReorderService {
    private final ReorderRepository reorderRepository;
    private final ProductColorSizeRepository productColorSizeRepository;
    private final ReorderDslRepository reorderDslRepository;
    private final DepletionDslRepository depletionDslRepository;
    private final BrandUserRepository brandUserRepository;

    @Transactional(readOnly = true)
    public List<ReorderResponse.ReorderListRes> getReorderProductionList(@Valid ReorderRequest.ReorderSearchParam searchParam) {
      Long userId = null;
      if (searchParam.isUserOnly()){
        userId = Optional.ofNullable(AuthUtil.getSessionUser())
            .map(sessionUser -> sessionUser.getUserId())
            .orElse(null);
      }

      return reorderDslRepository.searchReorderProduction(searchParam.getBrandLineCode(), searchParam.getYear(), searchParam.getSeasonCode(), searchParam.getItemCodes(), searchParam.getGraphicCodes(), searchParam.getProductCodes(), searchParam.getStatus(), userId);
    }

    @Transactional
    public Long post(Create dto){
        ProductColorSize pcs = productColorSizeRepository.findOneByCode(dto.getFullProductCode());

        Reorder reorder = Reorder.builder()
                .brandLine(pcs.getBrandLine())
                .productCode(pcs.getProductCode())
                .colorCode(pcs.getInfoColor().getCode())
                .fullProductCode(dto.getFullProductCode() + dto.getQuantity() + LocalDateTime.now().toLocalTime().toString())
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
    public List<ListRes> getReorderList(SearchParam searchParam) {

        if(searchParam.getBrandLineCode() == null){
            searchParam.setBrandLineCode(brandUserRepository.findMainBrandByUserId(AuthUtil.getUserId()).getBrandLineCode());
        }

        if(searchParam.getSeasonCode() == null){
            searchParam.setSeasonCode(SeasonCode.recentSeasonCode());
        }

        if(searchParam.getYear() == null || searchParam.getYear() == 0){
            searchParam.setYear(LocalDate.now().getYear());
        }

        if(searchParam.getSearchDate() == null||searchParam.getSearchDate().getFrom() == null || searchParam.getSearchDate().getTo() == null){
            searchParam.setSearchDate(new DateContainer(
                    LocalDate.now().minusDays(15),
                    LocalDate.now()
            ));
        }

        List<Product> products = reorderDslRepository.getReorderList(
                searchParam.getBrandLineCode(),
                searchParam.getYear(),
                searchParam.getSeasonCode(),
                searchParam.getItemCodes(),
                searchParam.getGraphicCodes(),
                searchParam.getProductCodes()
        );

        List<String> fullProductCodes = products.stream()
                .map(Product::getFullProductCode)
                .toList();

        List<ReorderSearch> results =  reorderDslRepository.getReorderStats(
                fullProductCodes,
                searchParam.getSearchDate().getTo(),
                searchParam.getSearchDate().getFrom(),
                searchParam.getWareHouseId(),
                searchParam.getDistChannel()
        );

        List<DepletionResponse.ListRes> depletionLevels = depletionDslRepository.getActiveDepletionLevels(searchParam.getBrandLineCode());

        List<User> users = reorderRepository.findLatestReorderUser(fullProductCodes);

        List<ListRes> combinedList = products.stream()
                .map(product -> {
                    ReorderSearch result = results.stream()
                        .filter(r -> product.getFullProductCode().equals(r.getFullProductCode()))
                        .findFirst()
                        .orElse(null);

                    ListRes listRes;

                    if (result != null) {
                        listRes = ListRes.builder()
                            .fullProductCode(product.getFullProductCode())
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
                            .reorderAt(result.getReorderAt())
                            .build();
                    } else {
                      listRes = null;
                    }

                  if (result != null) {
                        users.stream()
                            .filter(user -> user.getFullProductCode().equals(result.getFullProductCode()))
                            .findFirst()
                            .ifPresent(user -> {
                                    listRes.setReorderAt(user.getUpdatedAt());
                                    listRes.setReorderBy(UserByResponse.builder()
                                        .id(user.getId())
                                        .name(user.getName())
                                        .imageUrl(user.getImageUrl())
                                        .build()
                                    );
                                }
                            );
                    }

                    return listRes;
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