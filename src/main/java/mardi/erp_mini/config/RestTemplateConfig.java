package mardi.erp_mini.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        factory.setConnectTimeout(5_000); // 연결 타임아웃 (ms)
//        factory.setReadTimeout(5_000);    // 응답 대기 타임아웃 (ms)

        return new RestTemplate();
    }
}
