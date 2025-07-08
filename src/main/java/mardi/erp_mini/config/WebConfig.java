package mardi.erp_mini.config;

import lombok.RequiredArgsConstructor;
import mardi.erp_mini.config.properties.CorsProperties;
import mardi.erp_mini.support.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final String ALLOWED_METHODS = "GET,HEAD,POST,DELETE,TRACE,OPTIONS,PATCH,PUT";

    private final CorsProperties corsProperties;
    private final LoggingInterceptor loggingInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsProperties.getAllowOrigins())
                .allowedMethods(ALLOWED_METHODS.split(","))
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600)
                .exposedHeaders(HttpHeaders.LOCATION);
    }
}
