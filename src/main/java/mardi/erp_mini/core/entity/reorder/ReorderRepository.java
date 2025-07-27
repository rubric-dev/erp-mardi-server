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
        SELECT r.full_prod_cd as fullProductCode, u.id as id, u.name as name, u.image_url as imageUrl, r.updated_at as updatedAt 
        FROM (
            SELECT full_prod_cd, modified_by, updated_at,
                   ROW_NUMBER() OVER (PARTITION BY full_prod_cd ORDER BY updated_at DESC) AS rn
            FROM reorder 
            WHERE full_prod_cd IN (:fullProductCodes)
        ) r
        join users u 
        on r.modified_by = u.id
        WHERE rn = 1
        """
    )
    public List<ReorderResponse.User> findLatestReorderUser(List<String> fullProductCodes);

}
