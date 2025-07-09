package mardi.erp_mini.core.entity.product;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> search(String productCode, String name, Long brandId, String seasonCode, String itemCode, Long graphicId, String colorCode, String statusCode) {
        final Pageable pageable = PageRequest.of(0, 10);

        final List<Product> results = queryFactory.selectFrom(QProduct.product)
                .where(
                        isBrandIdEqual(brandId),
                        isSeasonCodeEqual(seasonCode),
                        isItemCodeEqual(itemCode),
                        isGraphicIdEqual(graphicId),
                        isProductCodeEqual(productCode),
                        isStatusCodeEqual(statusCode)
                )
                .orderBy(QProduct.product.updatedAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return results;
    }

    private BooleanExpression isStatusCodeEqual(String statusCode) {
        return (statusCode == null) ? null : QProduct.product.statusCode.eq(statusCode);
    }

    private BooleanExpression isProductCodeEqual(String productCode) {
        return (productCode == null) ? null : QProduct.product.productCode.eq(productCode);
    }

    private BooleanExpression isGraphicIdEqual(Long graphicId) {
        return (graphicId == null) ? null : QProduct.product.graphic.id.eq(graphicId);
    }

    private BooleanExpression isItemCodeEqual(String itemCode) {
        return (itemCode == null) ? null : QProduct.product.infoItem.code.eq(itemCode);
    }

    private BooleanExpression isBrandIdEqual(final Long brandId){
        return (brandId == null)? null : QProduct.product.brand.id.eq(brandId);
    }

    private BooleanExpression isSeasonCodeEqual(String seasonCode) {
        return (seasonCode == null)? null : QProduct.product.infoSeason.code.eq(seasonCode);
    }

}
