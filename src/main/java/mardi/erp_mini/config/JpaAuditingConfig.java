package mardi.erp_mini.config;

import mardi.erp_mini.security.AuthUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> Optional.ofNullable(AuthUtil.getSessionUser())
                .map(user -> AuthUtil.getUserId())
                .or(() -> Optional.of(1L));
    }

}
