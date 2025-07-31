package mardi.erp_mini.core.entity.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("SELECT p FROM Product p WHERE p.brandLine.code = :brandLineCode order by p.year, p.seasonCode, p.name")
  List<Product> findByBrandLineCode(String brandLineCode);

}
