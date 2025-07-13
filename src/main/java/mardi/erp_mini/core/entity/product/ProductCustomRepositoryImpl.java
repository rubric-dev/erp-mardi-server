package mardi.erp_mini.core.entity.product;

import static mardi.erp_mini.core.entity.product.QGraphic.graphic;
import static mardi.erp_mini.core.entity.product.QProductColor.productColor;
import static mardi.erp_mini.core.entity.product.QProductColorGraphic.productColorGraphic;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.brand.QBrandLine;
import mardi.erp_mini.core.entity.user.QUser;
import mardi.erp_mini.core.response.ProductResponse;
import mardi.erp_mini.core.response.ProductResponse.GraphicListRes;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductColor> search(String productCode, String name, String brandLineCode, String seasonCode, String itemCode, String graphicCode, String statusCode, int page, int pageSize) {
        final List<ProductColor> results = queryFactory.selectFrom(productColor)
            .join(productColor.brandLine, QBrandLine.brandLine).fetchJoin()
            .leftJoin(graphic)
            .on(graphic.code.eq(graphicCode))
            .leftJoin(QUser.user)
            .on(QUser.user.id.eq(productColor.modifiedBy))
            .where(
                    isNameEqual(name),
                    isBrandIdEqual(brandLineCode),
                    isSeasonCodeEqual(seasonCode),
                    isItemCodeEqual(itemCode),
                    isProductCodeEqual(productCode)
            )
            .orderBy(QProduct.product.updatedAt.asc())
            .offset(page)
            .limit(pageSize)
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
                ProductResponse.CreatedBy.class,
                createdByUser.id,
                createdByUser.name,
                createdByUser.imageUrl
            ),
            graphic.createdAt,
            Projections.constructor(
                ProductResponse.UpdatedBy.class,
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
