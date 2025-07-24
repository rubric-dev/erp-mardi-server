package mardi.erp_mini.core.entity.product;

import static mardi.erp_mini.core.entity.product.QGraphic.graphic;
import static mardi.erp_mini.core.entity.product.QProductColor.productColor;
import static mardi.erp_mini.core.entity.product.QProductColorGraphic.productColorGraphic;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.entity.info.QInfoColor;
import mardi.erp_mini.core.entity.info.QInfoItem;
import mardi.erp_mini.core.entity.user.QUser;
import mardi.erp_mini.core.response.ProductResponse;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductResponse.Detail> search(List<String> productCodes, List<String> productNames, String brandLineCode, int year, SeasonCode seasonCode, List<String> itemCodes, List<String> graphicCodes){
        final List<ProductResponse.Detail> results = queryFactory
            .select(Projections.constructor(ProductResponse.Detail.class,
                productColor.id,
                productColor.imageUrl,
                productColor.name,
                productColor.productCode,
                productColor.year,
                SeasonCode.returnName(productColor.seasonCode),
                Projections.constructor(ProductResponse.InfoDetail.class,
                    QInfoColor.infoColor.id,
                    QInfoColor.infoColor.name,
                    QInfoColor.infoColor.code
                ),
                Projections.constructor(ProductResponse.InfoDetail.class,
                    QInfoItem.infoItem.id,
                    QInfoItem.infoItem.name,
                    QInfoItem.infoItem.code
                ),
                Projections.constructor(ProductResponse.InfoDetail.class,
                    graphic.id,
                    graphic.name,
                    graphic.code
                ),
                Projections.constructor(UserByResponse.class,
                    QUser.user.id,
                    QUser.user.name,
                    QUser.user.imageUrl
                ),
                productColor.updatedAt
            ))
            .from(productColor)
            .leftJoin(productColorGraphic)
            .on(productColorGraphic.productCode.eq(productColor.productCode))
            .leftJoin(graphic)
            .on(graphic.code.eq(productColorGraphic.graphicCode))
            .join(QInfoColor.infoColor).on(QInfoColor.infoColor.code.eq(productColor.infoColor.code))
            .join(QInfoItem.infoItem).on(QInfoItem.infoItem.code.eq(productColor.infoItem.code))
            .join(QUser.user).on(QUser.user.id.eq(productColor.modifiedBy))
            .where(
                (brandLineCode == null)? null : productColor.brandLine.code.eq(brandLineCode),
                (productNames == null || productNames.isEmpty()) ? null : productColor.name.in(productNames),
                (productCodes == null || productCodes.isEmpty()) ? null : productColor.productCode.in(productCodes),
                (graphicCodes == null || graphicCodes.isEmpty()) ? null : graphic.code.in(graphicCodes),
                (itemCodes == null || itemCodes.isEmpty()) ? null : productColor.infoItem.code.in(itemCodes),
                (year < 2000) ? null : productColor.year.eq(year),
                (seasonCode == null) ? null : productColor.seasonCode.eq(seasonCode)
            )
            .orderBy(productColor.updatedAt.asc())
            .fetch();

      return results;
    }

}
