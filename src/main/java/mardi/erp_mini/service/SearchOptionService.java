package mardi.erp_mini.service;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.DistributionChannel;
import mardi.erp_mini.core.entity.WareHouseRepository;
import mardi.erp_mini.core.entity.brand.BrandLineRepository;
import mardi.erp_mini.core.entity.brand.BrandUser;
import mardi.erp_mini.core.entity.brand.BrandUserRepository;
import mardi.erp_mini.core.entity.info.InfoItemRepository;
import mardi.erp_mini.core.entity.option.DepletionRepository;
import mardi.erp_mini.core.entity.product.GraphicRepository;
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
    private final WareHouseRepository wareHouseRepository;
    private final DepletionRepository depletionRepository;

    public List<SearchOptionResponse.Code> getBrandLines() {
        List<String> brandLineCodes = brandUserRepository.findAllByUserIdOrderByBrandLineCodeAsc(AuthUtil.getUserId())
                .stream().map(BrandUser::getBrandLineCode).toList();
        return brandLineRepository.findAllByCodeInOrderByCode(brandLineCodes).stream()
                .map(brandLine -> SearchOptionResponse.Code.builder()
                        .code(brandLine.getCode())
                        .name(brandLine.getName())
                        .build()
                ).toList();
    }

    public List<SearchOptionResponse.Code> getInfoItems() {
        return infoItemRepository.findAllByOrderByCodeAsc().stream()
                .map(infoItem -> SearchOptionResponse.Code.builder()
                        .code(infoItem.getCode())
                        .name(infoItem.getName())
                        .build()
                )
                .toList();
    }

    public List<SearchOptionResponse.Code> getGraphics(String brandLineCode) {
        return graphicRepository.findAllByBrandLineCodeOrderBySeqDesc(brandLineCode).stream()
                .map(brandLine -> SearchOptionResponse.Code.builder()
                        .code(brandLine.getCode())
                        .name(brandLine.getName())
                        .build()
                )
                .toList();
    }

    public List<SearchOptionResponse.Code> getDistChannels() {
        return Stream.of(DistributionChannel.values())
                .map(status -> SearchOptionResponse.Code.builder()
                        .code(status.getCode())
                        .name(status.getName())
                        .build()
                ).toList();
    }

    public List<SearchOptionResponse.Id> getWareHouses(String brandLineCode) {
        return wareHouseRepository.findByBrandLineCodeOrderByName(brandLineCode).stream()
                .map(wareHouse -> SearchOptionResponse.Id.builder()
                        .id(wareHouse.getId())
                        .name(wareHouse.getName())
                        .build()
                )
                .toList();
    }

    public List<SearchOptionResponse.Id> getDepletionLevels() {
        return depletionRepository.findAllByOrderBySeq().stream()
                .map(depletionLevel -> SearchOptionResponse.Id.builder()
                        .id(depletionLevel.getId())
                        .name(depletionLevel.getName())
                        .build()
                )
                .toList();
    }
}
