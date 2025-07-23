package mardi.erp_mini.core.entity.product;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;
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
    public List<ProductResponse.Detail> search(List<String> productCodes, List<String> productNames, String brandLineCode, String seasonCode, List<String> itemCodes, List<String> graphicCodes, StatusCode statusCode){
        final List<ProductResponse.Detail> results = queryFactory
            .select(Projections.constructor(ProductResponse.Detail.class,
                productColor.id,
                productColor.imageUrl,
                productColor.name,
                productColor.productCode,
                productColor.colorCode,
                Projections.constructor(ProductResponse.InfoDetail.class,
                    QInfoSeason.infoSeason.id,
                    QInfoSeason.infoSeason.name,
                    QInfoSeason.infoSeason.code
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
            .join(QUser.user).on(QUser.user.id.eq(productColor.modifiedBy))
            .where(
                isBrandLineCodeEqual(brandLineCode),
                isProductNameIn(productNames),
                isProductCodeIn(productCodes),
                graphic.code.in(graphicCodes),
                isItemCodeIn(itemCodes),
                isSeasonCodeEqual(seasonCode)
            )
            .orderBy(productColor.updatedAt.asc())
            .fetch();

      return results;
    }

  @Override
  public List<ProductResponse.GraphicGroupListRes> searchGraphicGroup(String brandLineCode) {
    QUser createdByUser = new QUser("createdBy");
    QUser updatedByUser = new QUser("updatedBy");

    return queryFactory
        .select(Projections.constructor(
            ProductResponse.GraphicGroupListRes.class,
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
            productColorGraphic.colorCode.eq(productColor.colorCode))
        .leftJoin(createdByUser)
        .on(graphic.createdBy.eq(createdByUser.id))
        .leftJoin(updatedByUser)
        .on(graphic.modifiedBy.eq(updatedByUser.id))
        .where(graphic.brandLine.code.eq(brandLineCode))
        .groupBy(
            graphic.id, graphic.seq, graphic.code, graphic.name, graphic.createdAt, graphic.updatedAt,
            createdByUser.id, createdByUser.name, createdByUser.imageUrl,
            updatedByUser.id, updatedByUser.name, updatedByUser.imageUrl
        )
        .orderBy(graphic.seq.asc())
        .fetch();
    }

    private BooleanExpression isBrandLineCodeEqual(final String brandLineCode){
      return (brandLineCode == null)? null : productColor.brandLine.code.eq(brandLineCode);
    }

    private BooleanExpression isProductNameIn(List<String> productNames) {
      return (productNames == null || productNames.isEmpty()) ? null : productColor.name.in(productNames);
    }

    private BooleanExpression isProductCodeIn(List<String> productCodes) {
      return (productCodes == null || productCodes.isEmpty()) ? null : productColor.productCode.in(productCodes);
    }

    private BooleanExpression isItemCodeIn(List<String> itemCodes) {
      return (itemCodes == null || itemCodes.isEmpty()) ? null : productColor.productCode.in(itemCodes);
    }

    private BooleanExpression isSeasonCodeEqual(String seasonCode) {
      return (seasonCode == null || StringUtil.isBlank(seasonCode)) ? null : productColor.infoSeason.code.eq(seasonCode);
    }
}
