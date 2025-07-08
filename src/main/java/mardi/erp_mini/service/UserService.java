package mardi.erp_mini.service;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.auth.RolesRepository;
import mardi.erp_mini.core.entity.auth.UserAuthRepository;
import mardi.erp_mini.core.entity.brand.BrandRepository;
import mardi.erp_mini.core.entity.brand.BrandUserRepository;
import mardi.erp_mini.core.entity.user.User;
import mardi.erp_mini.core.entity.user.UserRepository;
import mardi.erp_mini.core.response.UserResponse;
import mardi.erp_mini.security.AuthUtil;
import mardi.erp_mini.support.excel.ExcelManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final BrandRepository brandRepository;
    private final UserAuthRepository userAuthRepository;
    private final BrandUserRepository brandUserRepository;

    @Transactional
    public Long create() {
        return null;
    }

    @Transactional(readOnly = true)
    public User getAuthenticatedUser() {
        Long userId = AuthUtil.getUserId();
        return userRepository.findOneById(userId);
    }

    @Transactional(readOnly = true)
    public UserResponse.Detail getUserById(Long id) {
        User user = userRepository.findOneById(id);

        User sessionUser = getAuthenticatedUser();
        // if (!Objects.equals(sessionUser.getCompanyId(), user.getCompanyId())) throw new DataAccessDenyException();

        UserResponse.Detail result = UserResponse.Detail.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .isSystemAdmin(user.isSystemAdmin())
                .build();

        return result;
    }
}
