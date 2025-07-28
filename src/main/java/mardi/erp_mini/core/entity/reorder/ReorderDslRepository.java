package mardi.erp_mini.core.entity.reorder;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotEmpty;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.entity.DistributionChannel;
import mardi.erp_mini.core.entity.info.QInfoColor;
import mardi.erp_mini.core.entity.info.QInfoSize;
import mardi.erp_mini.core.entity.product.QGraphic;
import mardi.erp_mini.core.entity.product.QProductColorGraphic;
import mardi.erp_mini.core.entity.product.QProductColorSize;
import mardi.erp_mini.core.entity.product.QProductionLeadTime;
import mardi.erp_mini.core.entity.product.QProductionMoq;
import mardi.erp_mini.core.entity.product.SeasonCode;
import mardi.erp_mini.core.entity.user.QUser;
import mardi.erp_mini.core.response.ReorderResponse;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReorderDslRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public List<ReorderSearch> getReorderStats(List<String> fullProductCodes, LocalDate to,LocalDate from, Long wareHouseId, DistributionChannel distributionChannel) {

        return entityManager.createNamedQuery("ReorderSearch.nativeQuery", ReorderSearch.class)
                .setParameter("products", fullProductCodes)
                .setParameter("to", Date.valueOf(to))
                .setParameter("from", Date.valueOf(from))
                .setParameter("warehouseId", wareHouseId)
                .setParameter("distributionChannel", distributionChannel.getCode() )
                .getResultList();
    }

    public List<ReorderResponse.Product> getReorderList(@NotEmpty String brandLineCode,int year, SeasonCode seasonCode, List<String> itemCodes, List<String> graphicCodes, List<String> productCodes) {
        QProductionMoq moq = QProductionMoq.productionMoq;
        QProductionLeadTime lt = QProductionLeadTime.productionLeadTime;
        QProductColorSize pcs = QProductColorSize.productColorSize;
        QProductColorGraphic pcg = QProductColorGraphic.productColorGraphic;

        return queryFactory.select(
                        Projections.constructor(
                                ReorderResponse.Product.class,
                                pcs.fullProductCode,
                                pcs.imageUrl,
                                pcs.productCode,
                                pcs.name,
                                pcs.infoColor.code,
                                QInfoColor.infoColor.name,
                                pcs.infoSize.code,
                                QInfoSize.infoSize.name,
                                pcg.graphicCode,
                                QGraphic.graphic.name,
                                moq.moqQty,
                                lt.leadTime
                        )
                ).from(pcs)
                .join(pcg).on(pcs.productCode.eq(pcg.productCode).and(pcs.infoColor.code.eq(pcg.colorCode)))
                .join(QInfoColor.infoColor).on(pcs.infoColor.code.eq(QInfoColor.infoColor.code))
                .join(QInfoSize.infoSize).on(pcs.infoSize.code.eq(QInfoSize.infoSize.code))
                .join(QGraphic.graphic).on(pcg.graphicCode.eq(QGraphic.graphic.code))
                .join(moq).on(pcs.productCode.eq(moq.productCode).and(pcs.infoColor.code.eq(moq.infoColor.code)))
                .join(lt).on(pcs.productCode.eq(lt.productCode).and(pcs.infoColor.code.eq(lt.infoColor.code)))
                .where(
                        pcs.brandLine.code.eq(brandLineCode),
                        pcs.year.eq(year),
                        seasonCode != null? null : pcs.seasonCode.eq(seasonCode),
                        itemCodes != null && !itemCodes.isEmpty() ? pcs.infoItem.code.in(itemCodes) : null,
                        graphicCodes != null && !graphicCodes.isEmpty() ? pcg.graphicCode.in(graphicCodes) : null,
                        productCodes != null && !productCodes.isEmpty() ? pcs.productCode.in(productCodes) : null
                )
                .fetch();
    }

  public List<ReorderResponse.ReorderListRes> searchReorderProduction(String brandLineCode, Integer year, SeasonCode seasonCode, List<String> itemCodes, List<String> graphicCodes, List<String> productCodes,
      Reorder.Status status, Long userId) {
    QReorder reorder = QReorder.reorder;
    QProductColorSize pcs = QProductColorSize.productColorSize;
    QInfoColor infoColor = QInfoColor.infoColor;
    QInfoSize infoSize = QInfoSize.infoSize;
    QGraphic graphic = QGraphic.graphic;
    QUser confirmedUser = QUser.user;
    QUser updatedUser = QUser.user;

    return queryFactory
        .select(Projections.constructor(
            ReorderResponse.ReorderListRes.class,
            reorder.fullProductCode,
            pcs.imageUrl,
            pcs.name,
            graphic.name,
            infoColor.name,
            infoSize.name,
            reorder.status,
            reorder.quantity,
            Projections.constructor(
                UserByResponse.class,
                updatedUser.id,
                updatedUser.name,
                updatedUser.imageUrl
            ),
            reorder.updatedAt,
            Projections.constructor(
                UserByResponse.class,
                confirmedUser.id,
                confirmedUser.name,
                confirmedUser.imageUrl
            ),
            reorder.confirmedAt
        ))
        .from(reorder)
        .join(pcs).on(reorder.fullProductCode.eq(pcs.fullProductCode))
        .join(graphic).on(reorder.graphicCode.eq(graphic.code))
        .join(infoColor).on(reorder.colorCode.eq(infoColor.code))
        .join(infoSize).on(reorder.infoSize.code.eq(infoSize.code))
        .join(updatedUser).on(updatedUser.id.eq(reorder.modifiedBy))
        .leftJoin(confirmedUser).on((confirmedUser.id.eq(reorder.confirmUserId)))
        .where(
            reorder.brandLine.code.eq(brandLineCode),
            pcs.seasonCode.eq(seasonCode),
            pcs.year.eq(year),
            itemCodes != null && !itemCodes.isEmpty() ? reorder.productCode.in(itemCodes) : null,
            graphicCodes != null && !graphicCodes.isEmpty() ? reorder.graphicCode.in(graphicCodes) : null,
            productCodes != null && !productCodes.isEmpty() ? reorder.fullProductCode.in(productCodes) : null,
            status != null ? reorder.status.eq(status) : null,
            userId != null? reorder.modifiedBy.eq(userId) : null
        )
        .orderBy(reorder.updatedAt.desc())
        .fetch();
  }
}