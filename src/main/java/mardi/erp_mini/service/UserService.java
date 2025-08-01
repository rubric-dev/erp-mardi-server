package mardi.erp_mini.service;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.brand.BrandCustomRepository;
import mardi.erp_mini.core.entity.brand.BrandUserRepository;
import mardi.erp_mini.core.entity.user.User;
import mardi.erp_mini.core.entity.user.UserCustomRepository;
import mardi.erp_mini.core.response.UserResponse;
import mardi.erp_mini.security.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserCustomRepository userCustomRepository;
    private final BrandCustomRepository brandCustomRepository;
    private final BrandUserRepository brandUserRepository;

    @Transactional(readOnly = true)
    public User getAuthenticatedUser() {
        Long userId = AuthUtil.getUserId();
        return userCustomRepository.findOneById(userId);
    }

    @Transactional(readOnly = true)
    public UserResponse.Detail getUserById(Long userId) {
        User user = userCustomRepository.findOneById(userId);

        //TODO: 삭제된 브랜드가 있는 것에 대한 오류 여부
        List<Long> brandIds = brandUserRepository.findBrandIdByUserIdAndIsDeletedIsFalse(userId);

        return UserResponse.Detail.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .brands(getBrandDetails(brandIds))
                .build();
    }

    //TODO: 사용자 목록 조회시 표출되는 데이터 와 검색 가능한 파라마터 설정
    @Transactional(readOnly = true)
    public List<UserResponse.ListRes> getUserList() {
        return userCustomRepository.findAll().stream()
                .map(user -> UserResponse.ListRes.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build())
                .toList();
    }

    private List<UserResponse.BrandDetail> getBrandDetails(List<Long> brandIds){
        return brandCustomRepository.findByIds(brandIds)
                .stream()
                .map(brand -> UserResponse.BrandDetail.builder()
                                .id(brand.getId())
                                .name(brand.getName())
                                .build()
                )
                .toList();
    }

    public void deleteUser(Long userId) {
        userCustomRepository.deleteUser(userId);
    }
}