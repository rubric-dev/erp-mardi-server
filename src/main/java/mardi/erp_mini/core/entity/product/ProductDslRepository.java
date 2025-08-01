package mardi.erp_mini.core.entity.product;

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
public class ProductDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<ProductResponse.Detail> search(List<String> productCodes, List<String> productNames, String brandLineCode, int year, SeasonCode seasonCode, List<String> itemCodes, List<String> graphicCodes){
        QProductColor productColor = QProductColor.productColor;
        QProductGraphic productGraphic = QProductGraphic.productGraphic;
        QGraphic graphic = QGraphic.graphic;

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
            .leftJoin(productGraphic)
            .on(productGraphic.productCode.eq(productColor.productCode))
            .leftJoin(graphic)
            .on(graphic.code.eq(productGraphic.graphicCode))
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

    public List<ProductResponse.ProductDetail> getProducts(String brandLineCode, Integer year, SeasonCode seasonCode, List<String> itemCodes, List<String> graphicCodes, List<String> productCodes, List<String> productNames, Boolean isSteadySeller) {
        QProduct product = QProduct.product;
        QProductGraphic productGraphic = QProductGraphic.productGraphic;
        QGraphic graphic = QGraphic.graphic;
        boolean isProductColor = false;

        return queryFactory
                .select(Projections.constructor(ProductResponse.ProductDetail.class,
                        product.id,
                        product.imageUrl,
                        product.name,
                        product.productCode,
                        product.year,
                        SeasonCode.returnName(product.seasonCode),
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
                        product.updatedAt
                ))
                .from(product)
                .leftJoin(productGraphic)
                .on(productGraphic.productCode.eq(product.productCode))
                .leftJoin(graphic)
                .on(graphic.code.eq(productGraphic.graphicCode))
                .join(QInfoItem.infoItem).on(QInfoItem.infoItem.code.eq(product.infoItem.code))
                .join(QUser.user).on(QUser.user.id.eq(product.modifiedBy))
                .where(
                        (brandLineCode == null)? null : product.brandLine.code.eq(brandLineCode),
                        (productNames == null || productNames.isEmpty()) ? null : product.name.in(productNames),
                        (productCodes == null || productCodes.isEmpty()) ? null : product.productCode.in(productCodes),
                        (itemCodes == null || itemCodes.isEmpty()) ? null : product.infoItem.code.in(itemCodes),
                        (year == null) ? null : product.year.eq(year),
                        (seasonCode == null) ? null : product.seasonCode.eq(seasonCode),
                        isSteadySeller ? product.isSteadySeller.isTrue() : null
                )
                .orderBy(product.updatedAt.desc())
                .fetch();
    }

    public List<ProductResponse.Detail> getProductColors(String brandLineCode, Integer year, SeasonCode seasonCode, List<String> itemCodes, List<String> graphicCodes, List<String> productCodes, List<String> productNames, Boolean isSteadySeller) {
        QProductColor productColor = QProductColor.productColor;
        QProductGraphic productGraphic = QProductGraphic.productGraphic;
        QGraphic graphic = QGraphic.graphic;

        return queryFactory
                .select(Projections.constructor(ProductResponse.Detail.class,
                        productColor.id,
                        productColor.imageUrl,
                        productColor.name,
                        productColor.productCode,
                        productColor.year,
                        SeasonCode.returnName(productColor.seasonCode),
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
                        productColor.updatedAt,
                        Projections.constructor(ProductResponse.InfoDetail.class,
                                QInfoColor.infoColor.id,
                                QInfoColor.infoColor.name,
                                QInfoColor.infoColor.code
                        )
                ))
                .from(productColor)
                .leftJoin(productGraphic)
                .on(productGraphic.productCode.eq(productColor.productCode))
                .leftJoin(graphic)
                .on(graphic.code.eq(productGraphic.graphicCode))
                .join(QInfoColor.infoColor).on(QInfoColor.infoColor.code.eq(productColor.infoColor.code))
                .join(QInfoItem.infoItem).on(QInfoItem.infoItem.code.eq(productColor.infoItem.code))
                .join(QUser.user).on(QUser.user.id.eq(productColor.modifiedBy))
                .where(
                        (brandLineCode == null)? null : productColor.brandLine.code.eq(brandLineCode),
                        (productNames == null || productNames.isEmpty()) ? null : productColor.name.in(productNames),
                        (productCodes == null || productCodes.isEmpty()) ? null : productColor.productCode.in(productCodes),
                        (itemCodes == null || itemCodes.isEmpty()) ? null : productColor.infoItem.code.in(itemCodes),
                        (year == null) ? null : productColor.year.eq(year),
                        (seasonCode == null) ? null : productColor.seasonCode.eq(seasonCode),
                        isSteadySeller ? productColor.isSteadySeller.isTrue() : null
                )
                .orderBy(productColor.updatedAt.desc())
                .fetch();
    }

}
