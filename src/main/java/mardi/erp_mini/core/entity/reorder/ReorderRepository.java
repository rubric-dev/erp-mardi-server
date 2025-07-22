package mardi.erp_mini.core.entity.reorder;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.core.response.ReorderResponse;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;

public interface ReorderRepository extends JpaRepository<Reorder, Long> {
    @Nonnull
    default Reorder findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException("reorder not found. id : " + id));
    }


    @NativeQuery("""
        SELECT r.product_color_size_id as productColorSizeId, r.graphic_cd as graphicCode, u.id as id, u.name as name, u.image_url as imageUrl, r.updated_at as updatedAt 
        FROM (
            SELECT product_color_size_id, graphic_cd, modified_by, updated_at,
                   ROW_NUMBER() OVER (PARTITION BY product_color_size_id, graphic_cd ORDER BY reorder_date DESC) AS rn
            FROM reorder 
            WHERE product_color_size_id IN (:productColorSizeIds)
            and graphic_cd in (:graphicCodes)
        ) r
        join users u 
        on r.modified_by = u.id
        WHERE rn = 1
        """
    )
    public List<ReorderResponse.User> findLatestReorderUser(List<Long> productColorSizeIds, List<String> graphicCodes);

}
