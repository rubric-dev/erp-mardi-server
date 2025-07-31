package mardi.erp_mini.core.entity.product;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.entity.info.QInfoColor;
import mardi.erp_mini.core.entity.info.QInfoItem;
import mardi.erp_mini.core.entity.user.QUser;
import mardi.erp_mini.core.response.GraphicResponse;
import mardi.erp_mini.core.response.ProductResponse;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class GraphicDslRepository {

  private final JPAQueryFactory queryFactory;

  public List<GraphicResponse.ListRes> searchGraphicList(String brandLineCode) {
    QUser createdByUser = new QUser("createdBy");
    QUser updatedByUser = new QUser("updatedBy");
    QProductColor productColor = QProductColor.productColor;
    QProductColorGraphic productColorGraphic = QProductColorGraphic.productColorGraphic;
    QGraphic graphic = QGraphic.graphic;

    return queryFactory
        .select(Projections.constructor(
            GraphicResponse.ListRes.class,
            graphic.seq,
            Projections.constructor(
                ProductResponse.InfoDetail.class,
                graphic.id,
                graphic.code,
                graphic.name
            ),
            productColor.id.countDistinct().as("noOfStyles"),
            Projections.constructor(
                UserByResponse.class,
                createdByUser.id,
                createdByUser.name,
                createdByUser.imageUrl
            ),
            graphic.createdAt,
            Projections.constructor(
                UserByResponse.class,
                updatedByUser.id,
                updatedByUser.name,
                updatedByUser.imageUrl
            ),
            graphic.updatedAt
        ))
        .from(graphic)
        .leftJoin(productColorGraphic)
        .on(productColorGraphic.graphicCode.eq(graphic.code))
        .leftJoin(productColor)
        .on(productColorGraphic.productCode.eq(productColor.productCode),
            productColorGraphic.colorCode.eq(productColor.infoColor.code))
        .join(createdByUser)
        .on(graphic.createdBy.eq(createdByUser.id))
        .join(updatedByUser)
        .on(graphic.modifiedBy.eq(updatedByUser.id))
        .where(
            graphic.brandLine.code.eq(brandLineCode),
            graphic.isDeleted.isFalse()
        )
        .groupBy(
            graphic.id, graphic.seq, graphic.code, graphic.name, graphic.createdAt, graphic.updatedAt,
            createdByUser.id, createdByUser.name, createdByUser.imageUrl,
            updatedByUser.id, updatedByUser.name, updatedByUser.imageUrl
        )
        .orderBy(graphic.seq.asc(), graphic.name.asc())
        .fetch();
  }

  public List<ProductResponse.Detail> findProductColors(String graphicCode, String brandLineCode, List<String> productCodes, List<String> productNames, Integer year, SeasonCode seasonCode, List<String> itemCodes, boolean isSteadySeller) {
    return getProductColors(graphicCode, false, brandLineCode, productCodes, productNames, year, seasonCode, itemCodes, isSteadySeller);
  }

  public List<ProductResponse.Detail> findProductColors(String graphicCode, String brandLineCode){
    return getProductColors(graphicCode, true, brandLineCode, null, null, null, null, null, false);
  }

  private List<ProductResponse.Detail> getProductColors(String graphicCode, boolean isGraphicProduct, String brandLineCode, List<String> productCodes, List<String> productNames, Integer year, SeasonCode seasonCode, List<String> itemCodes, boolean isSteadySeller) {
    QProductColor productColor = QProductColor.productColor;
    QProductColorGraphic productColorGraphic = QProductColorGraphic.productColorGraphic;
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
            isGraphicProduct ? graphic.code.eq(graphicCode) :graphic.code.ne(graphicCode),
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
  public List<ProductResponse.ProductDetail> getProducts(String graphicCode, String brandLineCode,
      List<String> productCodes, List<String> productNames, Integer year, SeasonCode seasonCode,
      List<String> itemCodes) {
    QProduct product = QProduct.product;
    QProductColorGraphic productColorGraphic = QProductColorGraphic.productColorGraphic;
    QGraphic graphic = QGraphic.graphic;

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
        .leftJoin(productColorGraphic)
        .on(productColorGraphic.productCode.eq(product.productCode))
        .leftJoin(graphic)
        .on(graphic.code.eq(productColorGraphic.graphicCode))
        .join(QInfoItem.infoItem).on(QInfoItem.infoItem.code.eq(product.infoItem.code))
        .join(QUser.user).on(QUser.user.id.eq(product.modifiedBy))
        .where(
            graphic.code.ne(graphicCode),
            (brandLineCode == null)? null : product.brandLine.code.eq(brandLineCode),
            (productNames == null || productNames.isEmpty()) ? null : product.name.in(productNames),
            (productCodes == null || productCodes.isEmpty()) ? null : product.productCode.in(productCodes),
            (itemCodes == null || itemCodes.isEmpty()) ? null : product.infoItem.code.in(itemCodes),
            (year == null) ? null : product.year.eq(year),
            (seasonCode == null) ? null : product.seasonCode.eq(seasonCode)
        )
        .orderBy(product.updatedAt.desc())
        .fetch();
  }
}
