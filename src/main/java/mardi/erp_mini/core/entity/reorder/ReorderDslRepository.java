package mardi.erp_mini.core.entity.reorder;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.DistributionChannel;
import mardi.erp_mini.core.entity.product.*;
import mardi.erp_mini.core.response.ReorderResponse;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReorderDslRepository {

    private final JPAQueryFactory queryFactory;
    private final ReorderRepository reorderRepository;
    private final EntityManager entityManager;

    public List<ReorderSearch> getReorderStats(List<Long> productColorSizeIds, List<String> graphicCodes, LocalDate to,LocalDate from, Long wareHouseId, DistributionChannel distributionChannel) {

        return entityManager.createNamedQuery("ReorderSearch.nativeQuery", ReorderSearch.class)
                .setParameter("graphicCodes", graphicCodes)
                .setParameter("products", productColorSizeIds)
                .setParameter("to", Date.valueOf(to))
                .setParameter("from", Date.valueOf(from))
                .setParameter("warehouseId", wareHouseId)
                .setParameter("distributionChannel", distributionChannel.getCode() )
                .getResultList();
    }

    public List<ReorderResponse.Product> getReorderList(@NotEmpty String brandLineCode, String seasonCode, List<String> itemCodes, List<String> graphicCodes, List<String> productCodes) {
        QProductionMoq moq = QProductionMoq.productionMoq;
        QProductionLeadTime lt = QProductionLeadTime.productionLeadTime;
        QProductColorSize pcs = QProductColorSize.productColorSize;
        QProductColorGraphic graphic = QProductColorGraphic.productColorGraphic;
        return queryFactory.select(
                        Projections.constructor(
                                ReorderResponse.Product.class,
                                pcs.id,
                                pcs.imageUrl,
                                pcs.productCode,
                                pcs.name,
                                pcs.colorCode,
                                pcs.infoSize.code,
                                pcs.infoSize.name,
                                graphic.graphicCode,
                                moq.moqQty,
                                lt.leadTime
                        )
                ).from(pcs)
                .join(graphic).on(pcs.productCode.eq(graphic.productCode).and(pcs.colorCode.eq(graphic.colorCode)))
                .join(moq).on(pcs.productCode.eq(moq.productCode).and(pcs.colorCode.eq(moq.colorCode)))
                .leftJoin(lt).on(pcs.productCode.eq(lt.productCode).and(pcs.colorCode.eq(lt.colorCode)))
                .where(
                        pcs.brandLine.code.eq(brandLineCode),
                        seasonCode != null ? pcs.infoSeason.code.eq(seasonCode) : null,
                        itemCodes != null && !itemCodes.isEmpty() ? pcs.infoItem.code.in(itemCodes) : null,
                        graphicCodes != null && !graphicCodes.isEmpty() ? graphic.graphicCode.in(graphicCodes) : null,
                        productCodes != null && !productCodes.isEmpty() ? pcs.productCode.in(productCodes) : null
                )
                .fetch();
    }
}