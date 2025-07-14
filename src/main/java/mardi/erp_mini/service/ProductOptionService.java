package mardi.erp_mini.service;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.product.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductOptionService {
    private final ProductionLeadTimeRepository productionLeadTimeRepository;
    private final ProductionMoqRepository productionMoqRepository;
    private final ProductOptionDslRepository productOptionDslRepository;


    @Transactional
    public void updateMoq(Long moqId, int qty){
        ProductionMoq moq = productionMoqRepository.findOneById(moqId);
        moq.update(qty);
    }

    @Transactional
    public void updateLeadTime(Long leadTimeId, int leadTime){
        ProductionLeadTime productionLeadTime = productionLeadTimeRepository.findOneById(leadTimeId);
        productionLeadTime.updateLeadTime(leadTime);
    }


}

