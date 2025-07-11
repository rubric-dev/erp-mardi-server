package mardi.erp_mini.service;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.product.ProductionLeadTimeRepository;
import mardi.erp_mini.core.entity.product.ProductionMoqRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductOptionService {
    private final ProductionLeadTimeRepository productionLeadTimeRepository;
    private final ProductionMoqRepository productionMoqRepository;
}

