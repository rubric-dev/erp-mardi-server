package mardi.erp_mini.core.entity.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByBrandLineCodeOrderByYearAndName(String brandLineCode);
}
