package mardi.erp_mini.core.entity.product;

import static mardi.erp_mini.core.entity.product.QGraphic.graphic;
import static mardi.erp_mini.core.entity.product.QProductColor.productColor;
import static mardi.erp_mini.core.entity.product.QProductColorGraphic.productColorGraphic;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.entity.brand.QBrandLine;
import mardi.erp_mini.core.entity.info.QInfoItem;
import mardi.erp_mini.core.entity.info.QInfoSeason;
import mardi.erp_mini.core.entity.user.QUser;
import mardi.erp_mini.core.response.ProductResponse;
import mardi.erp_mini.core.response.ProductResponse.GraphicListRes;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

import static mardi.erp_mini.core.entity.product.QGraphic.graphic;
import static mardi.erp_mini.core.entity.product.QProductColor.productColor;
import static mardi.erp_mini.core.entity.product.QProductColorGraphic.productColorGraphic;

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
                Projections.constructor(UserByResponse.class,
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

  @Override
  public List<GraphicListRes> searchGraphicGroup(String brandLineCode, String seasonCode, List<String> itemCodes, List<String> productCodes, List<String> productNames, String statusCode) {
    QUser createdByUser = new QUser("createdBy");
    QUser updatedByUser = new QUser("updatedBy");

    //TODO: 무슨 seq인지와 브랜드, 시즌, 카테고리는 그래픽의 것인지 확인 필요
    return queryFactory
        .select(Projections.constructor(
            ProductResponse.GraphicListRes.class,
            graphic.seq,
            Projections.constructor(
                ProductResponse.GraphicDetail.class,
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
            productColorGraphic.colorCode.eq(productColor.colorCode))
        .leftJoin(createdByUser)
        .on(graphic.createdBy.eq(createdByUser.id))
        .leftJoin(updatedByUser)
        .on(graphic.modifiedBy.eq(updatedByUser.id))
        .where(
            graphic.brandLine.code.eq(brandLineCode),
            isItemCodeIn(itemCodes),
            isProductNameIn(productNames),
            isProductCodeIn(productCodes),
            isSeasonCodeEqual(seasonCode),
            isStatusCodeEqual(statusCode)
        )
        .groupBy(
            graphic.id, graphic.seq, graphic.code, graphic.name, graphic.createdAt, graphic.updatedAt,
            createdByUser.id, createdByUser.name, createdByUser.imageUrl,
            updatedByUser.id, updatedByUser.name, updatedByUser.imageUrl
        )
        .orderBy(graphic.seq.desc())
        .fetch();
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
    private BooleanExpression isStatusCodeEqual(String statusCode) {
        return (statusCode == null || StringUtil.isBlank(statusCode))? null : QProduct.product.infoSeason.code.eq(statusCode);
    }
}
