package mardi.erp_mini.core.entity.reorder;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.common.dto.response.UserByResponse;
import mardi.erp_mini.core.response.ReorderResponse;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReorderRepository extends JpaRepository<Reorder, Long> {
    @Nonnull
    default Reorder findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException("reorder not found. id : " + id));
    }


    @NativeQuery("SELECT r.product_color_size_id as productColorSizeId, r.graphic_cd as graphicCode, u.id as id, u.name as name, u.image_url as imageUrl, r.updated_at as updatedAt \n" +
            "FROM (\n" +
            "    SELECT product_color_size_id, graphic_cd, modified_by, updated_at,\n" +
            "           ROW_NUMBER() OVER (PARTITION BY product_color_size_id, graphic_cd ORDER BY reorder_date  DESC) AS rn\n" +
            "    FROM reorder \n" +
            "    WHERE product_color_size_id IN (:productColorSizeIds)\n" +
            "    and graphic_cd in (:graphicCodes)\n" +
            ") r\n" +
            "join users u \n" +
            "on r.modified_by = u.id\n" +
            "WHERE rn = 1")
    public List<ReorderResponse.User> findLatestReorderUser(List<Long> productColorSizeIds, List<String> graphicCodes);

}
