package mardi.erp_mini.core.entity.brand;

import jakarta.annotation.Nonnull;
import java.util.List;
import mardi.erp_mini.common.dto.response.ErrorCode;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandUserRepository extends JpaRepository<BrandUser, Long> {

    @Nonnull
    default List<BrandUser> findAllByUserId(@Nonnull Long userId) {
        List<BrandUser> result = this.findAllByUserIdOrderByBrandLineCodeAsc(userId);

        if (result == null || result.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_BRAND_USER.getMsg());
        }

        return result;
    }

    List<BrandUser> findAllByUserIdOrderByBrandLineCodeAsc(Long userId);

    BrandUser findFirstByUserIdOrderBySeq(Long userId);

    default BrandUser findMainBrandByUserId(Long userId){
        BrandUser result = this.findFirstByUserIdOrderBySeq(userId);

        if (result == null){
            throw new NotFoundException(ErrorCode.NOT_FOUND_BRAND_USER.getMsg());
        }

        return result;
    }
}
