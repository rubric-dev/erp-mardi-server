package mardi.erp_mini.core.entity.product;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.response.ProductOptionResponse;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductOptionDslRepository {
    private final JPAQueryFactory queryFactory;

    public List<ProductOptionResponse.LeadTimeList> getLeadTimeList(String productCode, String name, Collection<String> brandLineCode,
                                                                    String seasonCode, String itemCode, String graphicCode, String statusCode){

        //todo
        return List.of();
    }

}
