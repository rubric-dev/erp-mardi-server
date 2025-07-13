package mardi.erp_mini.core.entity.product;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.brand.QBrandLine;
import mardi.erp_mini.core.entity.info.QInfoItem;
import mardi.erp_mini.core.entity.info.QInfoSeason;
import mardi.erp_mini.core.entity.user.QUser;
import mardi.erp_mini.core.response.ProductResponse;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductResponse.Detail> search(List<String> productCodes, List<String> productNames, String brandLineCode, String seasonCode, List<String> itemCodes, List<String> graphicCodes, String statusCode){
        final List<ProductResponse.Detail> results = queryFactory
            .select(Projections.constructor(ProductResponse.Detail.class,
                QProductColor.productColor.id,
                QProductColor.productColor.imageUrl,
                QProductColor.productColor.name,
                QProductColor.productColor.productCode,
                QProductColor.productColor.colorCode,
                Projections.constructor(ProductResponse.InfoSeasonDetail.class,
                    QInfoSeason.infoSeason.id,
                    QInfoSeason.infoSeason.name,
                    QInfoSeason.infoSeason.code
                ),
                Projections.constructor(ProductResponse.InfoItemDetail.class,
                    QInfoItem.infoItem.id,
                    QInfoItem.infoItem.name,
                    QInfoItem.infoItem.code
                ),
                Projections.constructor(ProductResponse.UpdatedBy.class,
                    QUser.user.id,
                    QUser.user.name,
                    QUser.user.imageUrl
                ),
                QProductColor.productColor.updatedAt
            ))
            .from(QProductColor.productColor)
            .join(QProductColor.productColor.brandLine, QBrandLine.brandLine).fetchJoin()

            .join(QProductColorGraphic.productColorGraphic)
            .on(QProductColorGraphic.productColorGraphic.productCode.eq(QProductColor.productColor.productCode)) // Middle table join
            .join(QGraphic.graphic)
            .on(QGraphic.graphic.code.eq(QProductColorGraphic.productColorGraphic.graphicCode))
            .leftJoin(QUser.user).on(QUser.user.id.eq(QProductColor.productColor.modifiedBy))
            .where(
                isBrandLineCodeEqual(brandLineCode),
                isProductNameIn(productNames),
                isProductCodeIn(productCodes),
                isItemCodeIn(itemCodes),
                isSeasonCodeEqual(seasonCode)
            )
            .orderBy(QProduct.product.updatedAt.asc())
            .fetch();


      return results;
    }



    private BooleanExpression isBrandLineCodeEqual(final String brandLineCode){
      return (brandLineCode == null)? null : QProductColor.productColor.brandLine.code.eq(brandLineCode);
    }

    private BooleanExpression isProductNameIn(List<String> productNames) {
      return (productNames == null || productNames.isEmpty()) ? null : QProductColor.productColor.name.in(productNames);
    }

    private BooleanExpression isProductCodeIn(List<String> productCodes) {
      return (productCodes == null || productCodes.isEmpty()) ? null : QProductColor.productColor.productCode.in(productCodes);
    }

    private BooleanExpression isItemCodeIn(List<String> itemCodes) {
      return (itemCodes == null || itemCodes.isEmpty()) ? null : QProductColor.productColor.productCode.in(itemCodes);
    }

    private BooleanExpression isSeasonCodeEqual(String seasonCode) {
      return (seasonCode == null || StringUtil.isBlank(seasonCode))? null : QProduct.product.infoSeason.code.eq(seasonCode);
    }

}
