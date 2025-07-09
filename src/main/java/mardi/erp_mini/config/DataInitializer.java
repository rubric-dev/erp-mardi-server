package mardi.erp_mini.config;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.brand.Brand;
import mardi.erp_mini.core.entity.brand.BrandRepository;
import mardi.erp_mini.core.entity.info.InfoItem;
import mardi.erp_mini.core.entity.info.InfoItemRepository;
import mardi.erp_mini.core.entity.info.InfoSeason;
import mardi.erp_mini.core.entity.info.InfoSeasonRepository;
import mardi.erp_mini.core.entity.info.InfoSize;
import mardi.erp_mini.core.entity.info.InfoSizeRepository;
import mardi.erp_mini.core.entity.product.Graphic;
import mardi.erp_mini.core.entity.product.GraphicRepository;
import mardi.erp_mini.core.entity.product.Product;
import mardi.erp_mini.core.entity.product.ProductRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@RequiredArgsConstructor
@Component
public class DataInitializer{

  private final BrandRepository brandRepository;
  private final InfoItemRepository infoItemRepository;
  private final InfoSizeRepository infoSizeRepository;
  private final InfoSeasonRepository infoSeasonRepository;
  private final GraphicRepository graphicRepository;
  private final ProductRepository productRepository;

  @PostConstruct
  public void initData() {
    Brand brand1 = Brand.builder()
        .ownerId(1L)
        .name("Brand A")
        .imageUrl("https://example.com/images/brandA.png")
        .build();

    Brand brand2 = Brand.builder()
        .ownerId(2L)
        .name("Brand B")
        .imageUrl("https://example.com/images/brandB.png")
        .build();

    brandRepository.saveAll(List.of(brand1, brand2));

    InfoItem item1 = new InfoItem(null,"Shirts", "ITEM-01");
    InfoItem item2 = new InfoItem(null,"Pants", "ITEM-02");
    InfoItem item3 = new InfoItem(null,"Jackets", "ITEM-03");
    infoItemRepository.saveAll(List.of(item1, item2, item3));

    InfoSize size1 = new InfoSize(null,"Small", "SIZE-S");
    InfoSize size2 = new InfoSize(null,"Medium", "SIZE-M");
    InfoSize size3 = new InfoSize(null,"Large", "SIZE-L");
    infoSizeRepository.saveAll(List.of(size1, size2, size3));

    InfoSeason season1 = new InfoSeason(null, "Spring-Summer 2023", "SS23");
    InfoSeason season2 = new InfoSeason(null, "Fall-Winter 2023", "FW23");
    infoSeasonRepository.saveAll(List.of(season1, season2));

    Graphic graphic1 = new Graphic(null, "Graphic A");
    Graphic graphic2 = new Graphic(null, "Graphic B");
    graphicRepository.saveAll(List.of(graphic1, graphic2));

    Product product1 = Product.builder()
        .name("Product A")
        .imageUrl("https://example.com/productA.jpg")
        .barcode("BARCODE-A")
        .brand(brand1)
        .graphic(graphic1)
        .infoSeason(season1)
        .infoItem(item1)
        .productCode("PROD001")
        .colorCode("COLOR001")
        .infoSize(size1)
        .statusCode("STATUS-A")
        .isSteadySeller(true)
        .build();

    Product product2 = Product.builder()
        .name("Product B")
        .imageUrl("https://example.com/productB.jpg")
        .barcode("BARCODE-B")
        .brand(brand2)
        .graphic(graphic2)
        .infoSeason(season2)
        .infoItem(item2)
        .productCode("PROD002")
        .colorCode("COLOR002")
        .infoSize(size2)
        .statusCode("STATUS-B")
        .isSteadySeller(false)
        .build();

    productRepository.saveAll(List.of(product1, product2));
  }

}
