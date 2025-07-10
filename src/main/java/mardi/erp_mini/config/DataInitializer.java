package mardi.erp_mini.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.request.AuthRequest;
import mardi.erp_mini.core.entity.auth.UserAuth;
import mardi.erp_mini.core.entity.auth.UserAuthRepository;
import mardi.erp_mini.core.entity.brand.Brand;
import mardi.erp_mini.core.entity.brand.BrandRepository;
import mardi.erp_mini.core.entity.brand.BrandUser;
import mardi.erp_mini.core.entity.info.InfoItem;
import mardi.erp_mini.core.entity.info.InfoSeason;
import mardi.erp_mini.core.entity.product.ProductColor;
import mardi.erp_mini.core.entity.user.User;
import mardi.erp_mini.security.enums.RoleType;
import mardi.erp_mini.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Profile("local")
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final AuthService authService;
    private final BrandRepository brandRepository;

    @PostConstruct
    public void initializeData() {

        // Create sample brand
        Brand brand = Brand.builder()
                .name("MARDI Brand")
                .code("MRD001")
                .imageUrl("https://example.com/brand-logo.png")
                .ownerId(1L) // Assuming the admin user has ID 1
                .build();

        brandRepository.save(brand);

        authService.createUser(new AuthRequest.Create(
                "관리자",
                "admin",
                "admin@admin.com",
                List.of("MRD001")
        ));

    }
}