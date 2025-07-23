package mardi.erp_mini.core.entity.product;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.entity.info.QInfoColor;
import mardi.erp_mini.core.entity.info.QInfoSize;
import mardi.erp_mini.core.entity.user.QUser;
import mardi.erp_mini.core.response.ProductOptionResponse;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductOptionDslRepository {

  private final JPAQueryFactory queryFactory;

  public List<ProductOptionResponse.LeadTimeList> getLeadTimeList(String brandLineCode, List<String> productCodes, int year, List<SeasonCode> seasonCodes, List<String> itemCodes, List<String> graphicCodes) {
    QProductionLeadTime leadTime = QProductionLeadTime.productionLeadTime;
    QProductColor productColor = QProductColor.productColor;
    QInfoColor color = QInfoColor.infoColor;
    QUser user = QUser.user;

    return queryFactory
        .select(
            Projections.constructor(
                ProductOptionResponse.LeadTimeList.class,
                leadTime.id,
                leadTime.brandLine.code,
                productColor.imageUrl,
                leadTime.productCode,
                productColor.name,
                color.name,
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
            .and(leadTime.infoColor.code.eq(productColor.infoColor.code)))
        .join(color).on(leadTime.infoColor.code.eq(color.code))
        .join(user)
        .on(leadTime.modifiedBy.eq(user.id))
        .where(
            brandLineCode != null ? leadTime.brandLine.code.eq(brandLineCode) : null,
            (productCodes != null && !productCodes.isEmpty()) ? leadTime.productCode.in(productCodes) : null,
            (year < 2000) ? null : productColor.year.eq(year),
            (seasonCodes != null && !seasonCodes.isEmpty()) ? productColor.seasonCode.in(seasonCodes) : null,
            (itemCodes != null && !itemCodes.isEmpty()) ? productColor.infoItem.code.in(itemCodes) : null
        )
        .orderBy(
            leadTime.brandLine.code.asc(),
            leadTime.productCode.asc(),
            leadTime.infoColor.code.asc()
        )
        .fetch();

  }

  public List<ProductOptionResponse.MoqList> getMoqList(String brandLineCode, List<String> productCodes, int year, List<SeasonCode> seasonCodes, List<String> itemCodes, List<String> graphicCodes) {
    QProductionMoq moq = QProductionMoq.productionMoq;
    QProductColorSize pcs = QProductColorSize.productColorSize;
    QInfoColor color = QInfoColor.infoColor;
    QInfoSize size = QInfoSize.infoSize;
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
                color.name,
                size.name,
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
            .and(moq.infoColor.code.eq(pcs.infoColor.code))
            .and(moq.infoSize.code.eq(pcs.infoSize.code)))
        .join(color).on(moq.infoColor.code.eq(color.code))
        .join(size).on(moq.infoSize.code.eq(size.code))
        .join(user)
        .on(moq.modifiedBy.eq(user.id))
        .where(
            brandLineCode != null ? moq.brandLine.code.eq(brandLineCode) : null,
            (productCodes != null && !productCodes.isEmpty()) ? moq.productCode.in(productCodes) : null,
            (year < 2000) ? null : pcs.year.eq(year),
            (seasonCodes != null && !seasonCodes.isEmpty()) ? pcs.seasonCode.in(seasonCodes) : null,
            (itemCodes != null && !itemCodes.isEmpty()) ? pcs.infoItem.code.in(itemCodes) : null
        )
        .orderBy(moq.brandLine.code.asc(),
            moq.productCode.asc(),
            moq.infoColor.code.asc(),
            moq.infoSize.code.asc())
        .fetch();
  }

}
