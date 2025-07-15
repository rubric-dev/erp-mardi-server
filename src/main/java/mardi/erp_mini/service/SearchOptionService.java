package mardi.erp_mini.service;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.brand.BrandLineRepository;
import mardi.erp_mini.core.entity.brand.BrandUser;
import mardi.erp_mini.core.entity.brand.BrandUserRepository;
import mardi.erp_mini.core.entity.info.InfoItemRepository;
import mardi.erp_mini.core.entity.product.GraphicRepository;
import mardi.erp_mini.core.entity.product.StatusCode;
import mardi.erp_mini.core.response.SearchOptionResponse;
import mardi.erp_mini.security.AuthUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class SearchOptionService {
    private final BrandUserRepository brandUserRepository;
    private final BrandLineRepository brandLineRepository;
    private final InfoItemRepository infoItemRepository;
    private final GraphicRepository graphicRepository;

    public List<SearchOptionResponse> getBrandLines() {
        List<String> brandLineCodes = brandUserRepository.findAllByUserIdOrderByBrandLineCodeAsc(AuthUtil.getUserId())
                .stream().map(BrandUser::getBrandLineCode).toList();
        return brandLineRepository.findAllByCodeInOrderByCode(brandLineCodes).stream()
                .map(brandLine -> SearchOptionResponse.builder()
                        .code(brandLine.getCode())
                        .name(brandLine.getName())
                        .build()
                ).toList();
    }

    public List<SearchOptionResponse> getInfoItems() {
        return infoItemRepository.findAllByOrderByCodeAsc().stream()
                .map(infoItem -> SearchOptionResponse.builder()
                        .code(infoItem.getCode())
                        .name(infoItem.getName())
                        .build()
                )
                .toList();
    }

    public List<SearchOptionResponse> getGraphics(String brandLineCode) {
        return graphicRepository.findAllByBrandLineCodeOrderByCodeAsc(brandLineCode).stream()
                .map(brandLine -> SearchOptionResponse.builder()
                        .code(brandLine.getCode())
                        .name(brandLine.getName())
                        .build()
                )
                .toList();
    }

    public List<SearchOptionResponse> getStatus() {
        return Stream.of(StatusCode.values())
                .map(status -> SearchOptionResponse.builder()
                        .code(status.getCode())
                        .name(status.getName())
                        .build()
                ).toList();
    }
}
