package mardi.erp_mini.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    private final String[] allowOrigins;

    public CorsProperties(String[] allowOrigins) {
        this.allowOrigins = allowOrigins;
    }
}
