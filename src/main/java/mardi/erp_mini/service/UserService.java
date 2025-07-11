package mardi.erp_mini.service;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.auth.UserAuth;
import mardi.erp_mini.core.entity.auth.UserAuthRepository;
import mardi.erp_mini.core.entity.brand.*;
import mardi.erp_mini.core.entity.user.User;
import mardi.erp_mini.core.entity.user.UserCustomRepository;
import mardi.erp_mini.core.response.UserResponse;
import mardi.erp_mini.exception.NotFoundException;
import mardi.erp_mini.security.AuthUtil;
import mardi.erp_mini.security.enums.RoleType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserCustomRepository userCustomRepository;
    private final BrandLineRepository brandLineRepository;
    private final BrandUserRepository brandUserRepository;
    private final UserAuthRepository userAuthRepository;

    @Transactional(readOnly = true)
    public User getAuthenticatedUser() {
        Long userId = AuthUtil.getUserId();
        return userCustomRepository.findOneById(userId);
    }

    @Transactional(readOnly = true)
    public UserResponse.Detail getUserById(Long userId) {
        User user = userCustomRepository.findOneById(userId);
        UserAuth userAuth = userAuthRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new NotFoundException("no user found with usernamek: " + user.getId()));

        //TODO: 삭제된 브랜드가 있는 것에 대한 오류 여부
        List<String> brandLineCodes = brandUserRepository.findAllByUserId(userId)
                .stream()
                .map(BrandUser::getBrandLineCode)
                .toList();

        return UserResponse.Detail.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .brands(getBrandLineDetails(brandLineCodes))
                .isAdmin(userAuth.getRole() == RoleType.ADMIN)
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

    private List<UserResponse.BrandLineDetail> getBrandLineDetails(List<String> brandLineCodes){
        return brandLineRepository.findAllByCodeIn(brandLineCodes)
                .stream()
                .map(brand -> UserResponse.BrandLineDetail.builder()
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