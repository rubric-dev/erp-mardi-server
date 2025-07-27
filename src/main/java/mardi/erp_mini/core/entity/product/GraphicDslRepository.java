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

  public List<ProductResponse.Detail> findProducts(String graphicCode, String brandLineCode, List<String> productCodes, List<String> productNames, Integer year, SeasonCode seasonCode, List<String> itemCodes) {
    return getProducts(graphicCode, false, brandLineCode, productCodes, productNames, year, seasonCode, itemCodes);
  }

  public List<ProductResponse.Detail> findProducts(String graphicCode, String brandLineCode){
    return getProducts(graphicCode, true, brandLineCode, null, null, null, null, null);
  }

  private List<ProductResponse.Detail> getProducts(String graphicCode, boolean isGraphicProduct, String brandLineCode, List<String> productCodes, List<String> productNames, Integer year, SeasonCode seasonCode, List<String> itemCodes) {
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
            (seasonCode == null) ? null : productColor.seasonCode.eq(seasonCode)
        )
        .orderBy(productColor.updatedAt.desc())
        .fetch();


  }
}
