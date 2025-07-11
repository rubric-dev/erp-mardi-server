package mardi.erp_mini.core.entity.product;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.brand.QBrandLine;
import mardi.erp_mini.core.entity.user.QUser;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductColor> search(String productCode, String name, String brandLineCode, String seasonCode, String itemCode, String graphicCode, String statusCode, int page, int pageSize) {
        final List<ProductColor> results = queryFactory.selectFrom(QProductColor.productColor)
            .join(QProductColor.productColor.brandLine, QBrandLine.brandLine).fetchJoin()
            .leftJoin(QGraphic.graphic)
            .on(QGraphic.graphic.code.eq(graphicCode))
            .leftJoin(QUser.user)
            .on(QUser.user.id.eq(QProductColor.productColor.modifiedBy))
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

    private BooleanExpression isNameEqual(String name) {
        return (name == null || StringUtil.isBlank(name)) ? null : QProductColor.productColor.name.eq(name);
    }

    private BooleanExpression isProductCodeEqual(String productCode) {
        return (productCode == null || StringUtil.isBlank(productCode)) ? null : QProductColor.productColor.productCode.eq(productCode);
    }

    private BooleanExpression isItemCodeEqual(String itemCode) {
        return (itemCode == null || StringUtil.isBlank(itemCode)) ? null : QProduct.product.infoItem.code.eq(itemCode);
    }

    private BooleanExpression isBrandIdEqual(final String brandLineCode){
        return (brandLineCode == null)? null : QProductColor.productColor.brandLine.code.eq(brandLineCode);
    }

    private BooleanExpression isSeasonCodeEqual(String seasonCode) {
        return (seasonCode == null || StringUtil.isBlank(seasonCode))? null : QProduct.product.infoSeason.code.eq(seasonCode);
    }

}
