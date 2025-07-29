package mardi.erp_mini.service;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ScenarioRequest;
import mardi.erp_mini.common.BaseEntity;
import mardi.erp_mini.core.entity.info.InfoItem;
import mardi.erp_mini.core.entity.info.InfoItemRepository;
import mardi.erp_mini.core.entity.option.*;
import mardi.erp_mini.core.entity.brand.BrandLine;
import mardi.erp_mini.core.entity.brand.BrandLineRepository;
import mardi.erp_mini.core.entity.user.User;
import mardi.erp_mini.core.entity.user.UserRepository;
import mardi.erp_mini.core.response.ScenarioResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;
    private final DepletionRepository depletionRepository;
    private final ScenarioItemRepository scenarioItemRepository;
    private final BrandLineRepository BrandLineRepository;
    private final UserRepository userRepository;
    private final InfoItemRepository infoItemRepository;

    @Transactional
    public Long create(ScenarioRequest.Create request, String brandLineCode){
        BrandLine brandLine = BrandLineRepository.findOneByCode(brandLineCode);

        Scenario initScenario = Scenario.builder()
                .brandLine(brandLine)
                .name(request.getName())
                .build();

        Scenario scenario = scenarioRepository.save(initScenario);

        //새 시나리오에 모든 소진율 단계 추가
        List<DepletionLevel>depletionLevels = depletionRepository.findAll();
        List<InfoItem> infoItems = infoItemRepository.findAll();
        List<ScenarioItem> scenarioItem = new ArrayList<>();
        infoItems.forEach(infoItem -> depletionLevels.forEach(depletionLevel -> scenarioItem.add(ScenarioItem.builder()
                        .scenario(scenario)
                        .infoItem(infoItem)
                        .depletionLevel(depletionLevel)
                        .build())
        ));

        scenarioItemRepository.saveAll(scenarioItem);

        return scenario.getId();
    }

    @Transactional
    public void delete(Long scenarioId){
        scenarioRepository.findOneById(scenarioId).delete();
    }


    @Transactional
    public void updateActiveStatus(Long scenarioId, boolean isActive){
        Scenario scenario = scenarioRepository.findOneById(scenarioId);

        if (isActive) {
            Scenario existActiveScenario = scenarioRepository.findByBrandLineCodeAndIsActive(scenario.getBrandLine().getCode(), true);

            if (existActiveScenario != null) {
                existActiveScenario.deactivate();
            }

            scenario.activate();
        }
        else scenario.deactivate();
    }


    public List<ScenarioResponse.ListRes> getList(String brandCode) {
        BrandLine brandLine = BrandLineRepository.findOneByCode(brandCode);

        List<Scenario> scenarioList = scenarioRepository.findByBrandLine(brandLine);

        Set<Long> createUserIds = scenarioList.stream().map(BaseEntity::getCreatedBy).collect(Collectors.toSet());
        Set<Long> updateUserIds = scenarioList.stream().map(BaseEntity::getModifiedBy).collect(Collectors.toSet());

        createUserIds.addAll(updateUserIds);

        Map<Long, User> userMap = userRepository.findAllById(createUserIds).stream().collect(Collectors.toMap(User::getId, Function.identity()));

        return scenarioList.stream().map(it -> {
            User createUser = userMap.get(it.getCreatedBy());
            User updateUser = userMap.get(it.getModifiedBy());

            ScenarioResponse.UserContainer creatorRes = ScenarioResponse.UserContainer.builder()
                    .id(createUser.getId())
                    .name(createUser.getName())
                    .imageUrl(createUser.getImageUrl())
                    .build();

            ScenarioResponse.UserContainer updateUserRes = ScenarioResponse.UserContainer.builder()
                    .id(updateUser.getId())
                    .name(updateUser.getName())
                    .imageUrl(updateUser.getImageUrl())
                    .build();


            return ScenarioResponse.ListRes.builder()
                    .id(it.getId())
                    .name(it.getName())
                    .isActive(it.isActive())
                    .createdAt(it.getCreatedAt())
                    .updatedAt(it.getUpdatedAt())
                    .createUser(creatorRes)
                    .updateUser(updateUserRes)
                    .build();
        }).toList();
    }
}
