package mardi.erp_mini.core.entity.product;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.entity.info.QInfoColor;
import mardi.erp_mini.core.entity.info.QInfoItem;
import mardi.erp_mini.core.entity.user.QUser;
import mardi.erp_mini.core.response.ProductResponse;
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
            productColorGraphic.colorCode.eq(productColor.infoColor.code))
        .join(createdByUser)
        .on(graphic.createdBy.eq(createdByUser.id))
        .join(updatedByUser)
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

}
