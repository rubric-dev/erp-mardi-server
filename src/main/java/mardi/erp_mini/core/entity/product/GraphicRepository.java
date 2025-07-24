package mardi.erp_mini.core.entity.product;

import java.util.Optional;
import lombok.NonNull;
import mardi.erp_mini.common.dto.response.ErrorCode;
import mardi.erp_mini.exception.NotFoundException;
import org.hibernate.annotations.NotFound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GraphicRepository extends JpaRepository<Graphic, Long> {
    boolean existsByCode(String code);

    List<Graphic> findAllByBrandLineCodeOrderBySeqDesc(String brandLineCode);

    Optional<Graphic> findByCode(String code);

    @NonNull
    default Graphic findOneByGraphicCode(String code){
        return findByCode(code).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_GRAPHIC.getMsg()));
    }
}
