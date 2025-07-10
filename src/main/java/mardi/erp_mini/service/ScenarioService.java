package mardi.erp_mini.service;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ReorderRequest;
import mardi.erp_mini.common.BaseEntity;
import mardi.erp_mini.core.entity.Scenario;
import mardi.erp_mini.core.entity.ScenarioRepository;
import mardi.erp_mini.core.entity.brand.Brand;
import mardi.erp_mini.core.entity.brand.BrandRepository;
import mardi.erp_mini.core.entity.product.ProductColorSize;
import mardi.erp_mini.core.entity.product.ProductColorSizeRepository;
import mardi.erp_mini.core.entity.reorder.Reorder;
import mardi.erp_mini.core.entity.reorder.ReorderRepository;
import mardi.erp_mini.core.entity.user.User;
import mardi.erp_mini.core.entity.user.UserRepository;
import mardi.erp_mini.core.response.ScenarioResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long post(Long sessionUserId, String name, Long brandId){
        Brand brand = brandRepository.findOneById(brandId);

        Scenario initScenario = Scenario.builder()
                .brand(brand)
                .name(name)
                .build();

        Scenario scenario = scenarioRepository.save(initScenario);
        return scenario.getId();
    }

    @Transactional
    public void delete(Long sessionUserId, Long id){
        scenarioRepository.findOneById(id).delete();
    }


    @Transactional
    public void updateActiveStatus(Long sessionUserId, Long id, boolean isActive){
        Scenario scenario = scenarioRepository.findOneById(id);

        if (isActive) {
            Scenario existActiveScenario = scenarioRepository.findByBrandAndIsActive(scenario.getBrand(), true);

            existActiveScenario.deactivate();
            scenario.activate();
        }
        else scenario.deactivate();
    }


    public List<ScenarioResponse.ListRes> getList(Long sessionUserId, Long brandId) {
        Brand brand = brandRepository.findOneById(brandId);

        List<Scenario> scenarioList = scenarioRepository.findByBrand(brand);

        Set<Long> createUserIds = scenarioList.stream().map(BaseEntity::getCreatedBy).collect(Collectors.toSet());
        Set<Long> updateUserIds = scenarioList.stream().map(BaseEntity::getModifiedBy).collect(Collectors.toSet());

        createUserIds.addAll(updateUserIds);

        Map<Long, User> userMap = userRepository.findAllById(createUserIds).stream().collect(Collectors.toMap(User::getId, Function.identity()));

        return scenarioList.stream().map(it -> {
            User createUser = userMap.get(it.getCreatedBy());
            User updateUser = userMap.get(it.getUpdatedAt());

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
