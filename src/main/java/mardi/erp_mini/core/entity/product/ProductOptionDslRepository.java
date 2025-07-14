package mardi.erp_mini.core.entity.product;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.entity.user.QUser;
import mardi.erp_mini.core.response.ProductOptionResponse;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductOptionDslRepository {

  private final JPAQueryFactory queryFactory;

  //TODO: 상태 미적용
  public List<ProductOptionResponse.LeadTimeList> getLeadTimeList(String brandLineCode, List<String> productCodes, List<String> seasonCodes, List<String> itemCodes, List<String> graphicCodes, String statusCode) {
    QProductionLeadTime leadTime = QProductionLeadTime.productionLeadTime;
    QProductColor productColor = QProductColor.productColor;
    QUser user = QUser.user;

    return queryFactory
        .select(
            Projections.constructor(
                ProductOptionResponse.LeadTimeList.class,
                leadTime.id,
                leadTime.brandLine.code,
                productColor.imageUrl,
                leadTime.colorCode,
                leadTime.productCode,
                productColor.name,
                leadTime.leadTime,
                Projections.constructor(
                    UserByResponse.class,
                    user.id,
                    user.name,
                    user.imageUrl
                ),
                leadTime.updatedAt
            )
        )
        .from(leadTime)
        .join(productColor)
        .on(leadTime.productCode.eq(productColor.productCode)
            .and(leadTime.colorCode.eq(productColor.colorCode)))
        .leftJoin(user)
        .on(leadTime.modifiedBy.eq(user.id))
        .where(
            brandLineCode != null ? leadTime.brandLine.code.eq(brandLineCode) : null,
            (productCodes != null && !productCodes.isEmpty()) ? leadTime.productCode.in(productCodes) : null,
            (seasonCodes != null && !seasonCodes.isEmpty()) ? productColor.infoSeason.code.in(seasonCodes) : null,
            (itemCodes != null && !itemCodes.isEmpty()) ? productColor.infoItem.code.in(itemCodes) : null
        )
        .orderBy(
            leadTime.brandLine.code.asc(),
            leadTime.productCode.asc(),
            leadTime.colorCode.asc()
        )
        .fetch();

  }

  public List<ProductOptionResponse.MoqList> getMoqList(String brandLineCode, List<String> productCodes, List<String> seasonCodes, List<String> itemCodes, List<String> graphicCodes, String statusCode) {
    QProductionMoq moq = QProductionMoq.productionMoq;
    QProductColorSize pcs = QProductColorSize.productColorSize;
    QUser user = QUser.user;

    return queryFactory
        .select(
            Projections.constructor(
                ProductOptionResponse.MoqList.class,
                moq.id,
                moq.brandLine.code,
                pcs.imageUrl,
                moq.productCode,
                pcs.name,
                moq.colorCode,
                moq.infoSize.name,
                moq.moqQty,
                Projections.constructor(
                    UserByResponse.class,
                    user.id,
                    user.name,
                    user.imageUrl
                ),
                moq.updatedAt

            )
        )
        .from(moq)
        .join(pcs)
        .on(moq.brandLine.code.eq(pcs.brandLine.code)
            .and(moq.productCode.eq(pcs.productCode))
            .and(moq.colorCode.eq(pcs.colorCode))
            .and(moq.infoSize.code.eq(pcs.infoSize.code)))
        .leftJoin(user)
        .on(moq.modifiedBy.eq(user.id))
        .where(
            brandLineCode != null ? moq.brandLine.code.eq(brandLineCode) : null,
            (productCodes != null && !productCodes.isEmpty()) ? moq.productCode.in(productCodes) : null,
            (seasonCodes != null && !seasonCodes.isEmpty()) ? pcs.infoSeason.code.in(seasonCodes) : null,
            (itemCodes != null && !itemCodes.isEmpty()) ? pcs.infoItem.code.in(itemCodes) : null
        )
        .orderBy(moq.brandLine.code.asc(),
            moq.productCode.asc(),
            moq.colorCode.asc(),
            moq.infoSize.code.asc())
        .fetch();
  }

}
