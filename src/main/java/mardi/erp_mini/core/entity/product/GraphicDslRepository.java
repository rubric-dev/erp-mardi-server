package mardi.erp_mini.core.entity.product;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.entity.info.QInfoItem;
import mardi.erp_mini.core.entity.user.QUser;
import mardi.erp_mini.core.response.GraphicResponse;
import mardi.erp_mini.core.response.ProductResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class GraphicDslRepository {

  private final JPAQueryFactory queryFactory;

  public List<GraphicResponse.ListRes> searchGraphicList(String brandLineCode) {
    QUser createdByUser = new QUser("createdBy");
    QUser updatedByUser = new QUser("updatedBy");
    QProductColor productColor = QProductColor.productColor;
    QProductGraphic productGraphic = QProductGraphic.productGraphic;
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
        .leftJoin(productGraphic)
        .on(productGraphic.graphicCode.eq(graphic.code))
        .leftJoin(productColor)
        .on(productGraphic.productCode.eq(productColor.productCode))
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

  public List<GraphicResponse.ProductDetail> findProducts(String graphicCode, String brandLineCode){
    QProduct product = QProduct.product;
    QProductGraphic productGraphic = QProductGraphic.productGraphic;
    QGraphic graphic = QGraphic.graphic;

    return queryFactory
        .select(Projections.constructor(GraphicResponse.ProductDetail.class,
            product.id,
            product.imageUrl,
            product.name,
            product.productCode,
            product.year,
            SeasonCode.returnName(product.seasonCode),
            product.isSteadySeller,
            Projections.constructor(GraphicResponse.InfoDetail.class,
                QInfoItem.infoItem.id,
                QInfoItem.infoItem.name,
                QInfoItem.infoItem.code
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
        .join(QInfoItem.infoItem).on(QInfoItem.infoItem.code.eq(product.infoItem.code))
        .join(QUser.user).on(QUser.user.id.eq(product.modifiedBy))
        .where(
            productGraphic.graphicCode.eq(graphicCode),
            (brandLineCode == null)? null : product.brandLine.code.eq(brandLineCode)
        )
        .orderBy(product.updatedAt.desc())
        .fetch();
  }
}
