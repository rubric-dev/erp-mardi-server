package mardi.erp_mini.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;

@Configuration
public class AwsConfig {

    @Bean
    public SesV2Client getSesClient(){
        return SesV2Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .build();
    }
}
