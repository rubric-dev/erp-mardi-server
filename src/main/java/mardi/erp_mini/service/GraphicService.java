package mardi.erp_mini.service;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.GraphicRequest.Create;
import mardi.erp_mini.api.request.GraphicRequest.Product;
import mardi.erp_mini.common.dto.response.ErrorCode;
import mardi.erp_mini.core.entity.brand.BrandLineRepository;
import mardi.erp_mini.core.entity.product.*;
import mardi.erp_mini.core.response.GraphicResponse;
import mardi.erp_mini.exception.DuplicateCodeException;
import mardi.erp_mini.exception.InvalidValueException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GraphicService {

  private final GraphicRepository graphicRepository;
  private final GraphicDslRepository graphicDslRepository;
  private final ProductGraphicRepository productGraphicRepository;
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
  public int deleteGraphic(String graphicCode) {
    int deleted = productGraphicRepository.deleteProductGraphicByGraphicCode(graphicCode);
    graphicRepository.findOneByGraphicCode(graphicCode).delete();
    return deleted;
  }

  @Transactional(readOnly = true)
  public List<GraphicResponse.ProductDetail> getGraphicProducts(String graphicCode, String brandLineCode) {
    return graphicDslRepository.findProducts(graphicCode, brandLineCode);
  }

  @Transactional
  public void createGraphicProduct(String graphicCode, List<Product> request) {
    productGraphicRepository.saveAll(
        request.stream()
            .map(product -> ProductGraphic.of(product.getProductCode(), graphicCode))
            .toList()
    );
  }

  @Transactional
  public void deleteGraphicProduct(String graphicCode, List<Product> request) {
    List<ProductGraphic> productGraphics = productGraphicRepository.findByGraphicCodeAndProductCodeIn(graphicCode, request.stream().map(Product::getProductCode).toList());

    //해당 그래픽에 해당 품목이 없는 경우
    if (productGraphics.size() != request.size()) throw new InvalidValueException(ErrorCode.INVALID_VALUE.getMsg());

    productGraphicRepository.deleteAll(productGraphics);
  }
}
