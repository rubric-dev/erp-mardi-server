package mardi.erp_mini.service;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.request.ProductOptionRequest;
import mardi.erp_mini.core.entity.info.InfoSeasonRepository;
import mardi.erp_mini.core.entity.product.*;
import mardi.erp_mini.core.response.ProductOptionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductOptionService {
    private final ProductionLeadTimeRepository productionLeadTimeRepository;
    private final ProductionMoqRepository productionMoqRepository;
    private final ProductOptionDslRepository productOptionDslRepository;
    private final InfoSeasonRepository infoSeasonRepository;

    @Transactional
    public void updateMoq(ProductOptionRequest.MoqUpdate request){
        ProductionMoq moq = productionMoqRepository.findOneById(request.getMoqId());
        moq.update(request.getQty());
    }

    @Transactional
    public void updateLeadTime(ProductOptionRequest.LeadTimeUpdate request){
        ProductionLeadTime productionLeadTime = productionLeadTimeRepository.findOneById(request.getLeadTimeId());
        productionLeadTime.updateLeadTime(request.getLeadTime());
    }

    @Transactional(readOnly = true)
    public List<ProductOptionResponse.MoqList> getMoqList(ProductOptionRequest.MoqSearchParam searchParam) {
        //TODO: 브랜드가 없는 경우 로그인한 사용자 최상단 브랜드
        //TODO: 시즌이 없는 경우 최근 시즌

        return productOptionDslRepository.getMoqList(
                searchParam.getBrandLineCode(),
                searchParam.getProductCodes(),
                searchParam.getYear(),
                searchParam.getSeasonCodes(),
                searchParam.getItemCodes(),
                searchParam.getGraphicCodes()
        );
    }

    @Transactional(readOnly = true)
    public List<ProductOptionResponse.LeadTimeList> getLeadTimeList(ProductOptionRequest.LeadTimeSearchParam searchParam) {
        //TODO: 브랜드가 없는 경우 로그인한 사용자 최상단 브랜드
        //TODO: 시즌이 없는 경우 최근 시즌

        return productOptionDslRepository.getLeadTimeList(
                searchParam.getBrandLineCode(),
                searchParam.getProductCodes(),
                searchParam.getYear(),
                searchParam.getSeasonCodes(),
                searchParam.getItemCodes(),
                searchParam.getGraphicCodes()
        );
    }
}

