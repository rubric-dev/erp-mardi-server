package mardi.erp_mini.core.entity.product;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.response.ProductOptionResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductOptionDslRepository {
    private final JPAQueryFactory queryFactory;

    public List<ProductOptionResponse.LeadTimeList> getLeadTimeList(String brandLineCode, List<String> productCodes, List<String> seasonCodes, List<String> itemCodes, List<String> graphicCodes, String statusCode) {
        return List.of();
    }

    public List<ProductOptionResponse.MoqList> getMoqList(String brandLineCode, List<String> productCodes, List<String> seasonCodes, List<String> itemCodes, List<String> graphicCodes, String statusCode) {
        QProductionMoq moq = QProductionMoq.productionMoq;
        QProductColorSize pcs = QProductColorSize.productColorSize;

        return queryFactory
                .select(
                        Projections.constructor(
                                ProductOptionResponse.MoqList.class,
                                moq.id,
                                pcs.imageUrl,
                                moq.brandLine.code,
                                moq.productCode,
                                moq.colorCode,
                                moq.infoSize.code,
                                moq.moqQty
                        )
                )
                .from(moq)
                .join(pcs)
                .on(moq.brandLine.code.eq(pcs.brandLine.code)
                        .and(moq.productCode.eq(pcs.productCode))
                        .and(moq.colorCode.eq(pcs.colorCode))
                        .and(moq.infoSize.code.eq(pcs.infoSize.code)))
                .where(
                        brandLineCode != null ? moq.brandLine.code.eq(brandLineCode) : null,
                        (productCodes != null && !productCodes.isEmpty()) ? moq.productCode.in(productCodes) : null,
                        (seasonCodes != null && !seasonCodes.isEmpty()) ? pcs.infoSeason.code.in(seasonCodes) : null,
                        (itemCodes != null && !itemCodes.isEmpty()) ? pcs.infoItem.code.in(itemCodes) : null)
                .orderBy(moq.brandLine.code.asc(),
                        moq.productCode.asc(),
                        moq.colorCode.asc(),
                        moq.infoSize.code.asc())
                .fetch();
    }

}
