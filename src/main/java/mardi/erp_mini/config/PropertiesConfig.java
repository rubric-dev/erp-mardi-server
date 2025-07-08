package mardi.erp_mini.config;

import mardi.erp_mini.config.properties.CorsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({CorsProperties.class})
public class PropertiesConfig {

}
