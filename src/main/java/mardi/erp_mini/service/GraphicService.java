package mardi.erp_mini.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.GraphicRequest;
import mardi.erp_mini.api.request.GraphicRequest.Create;
import mardi.erp_mini.api.request.GraphicRequest.Product;
import mardi.erp_mini.core.entity.brand.BrandLineRepository;
import mardi.erp_mini.core.entity.product.Graphic;
import mardi.erp_mini.core.entity.product.GraphicDslRepository;
import mardi.erp_mini.core.entity.product.GraphicRepository;
import mardi.erp_mini.core.entity.product.ProductColorGraphic;
import mardi.erp_mini.core.entity.product.ProductColorGraphicRepository;
import mardi.erp_mini.core.entity.product.SeasonCode;
import mardi.erp_mini.core.response.GraphicResponse;
import mardi.erp_mini.core.response.ProductResponse;
import mardi.erp_mini.exception.DuplicateCodeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GraphicService {

  private final GraphicRepository graphicRepository;
  private final GraphicDslRepository graphicDslRepository;
  private final ProductColorGraphicRepository productColorGraphicRepository;
  private final BrandLineRepository brandLineRepository;

  @Transactional(readOnly = true)
  public List<GraphicResponse.ListRes> getGraphicGroupList(String brandLineCode) {
    return graphicDslRepository.searchGraphicList(brandLineCode);
  }

  @Transactional
  public void createGraphic(Create request) {

    if (graphicRepository.existsByCode(request.getCode())){
      throw new DuplicateCodeException();
    }

    graphicRepository.save(
        Graphic.of(
            brandLineRepository.findOneByCode(request.getBrandLineCode()),
            request.getName(),
            request.getCode(),
            request.getSeq()
        )
    );
  }

  @Transactional
  public void deleteGraphic(String graphicCode) {
    graphicRepository.findOneByGraphicCode(graphicCode).delete();
  }

  @Transactional(readOnly = true)
  public List<ProductResponse.Detail> getGraphicProductList(String graphicCode, String brandLineCode) {
    return graphicDslRepository.findProductColors(graphicCode, brandLineCode);
  }

  @Transactional
  public void createGraphicProduct(String graphicCode, List<Product> request) {
    productColorGraphicRepository.saveAll(
        request.stream()
            .map(product -> ProductColorGraphic.of(product.getProductCode(), product.getColorCode(), graphicCode))
            .toList()
    );
  }
}
