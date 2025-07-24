package mardi.erp_mini.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.GraphicRequest;
import mardi.erp_mini.api.request.GraphicRequest.Create;
import mardi.erp_mini.api.request.GraphicRequest.Product;
import mardi.erp_mini.core.entity.product.Graphic;
import mardi.erp_mini.core.entity.product.GraphicDslRepository;
import mardi.erp_mini.core.entity.product.GraphicRepository;
import mardi.erp_mini.core.entity.product.ProductColorGraphic;
import mardi.erp_mini.core.entity.product.ProductColorGraphicRepository;
import mardi.erp_mini.core.response.GraphicResponse;
import mardi.erp_mini.core.response.ProductResponse;
import mardi.erp_mini.core.response.ProductResponse.Detail;
import mardi.erp_mini.exception.DuplicateCodeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GraphicService {

  private final GraphicRepository graphicRepository;
  private final GraphicDslRepository graphicDslRepository;
  private final ProductColorGraphicRepository productColorGraphicRepository;

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
            request.getBrandLineCode(),
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
    return graphicDslRepository.findProducts(graphicCode, brandLineCode);
  }

  @Transactional(readOnly = true)
  public List<Detail> getProductListForGraphic(String graphicCode, GraphicRequest.SearchParam searchParam) {
    return graphicDslRepository.findProducts(
        graphicCode,
        searchParam.getBrandLineCode(),
        searchParam.getProductCodes(),
        searchParam.getProductNames(),
        searchParam.getYear(),
        searchParam.getSeasonCode(),
        searchParam.getItemCodes()
    );
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
