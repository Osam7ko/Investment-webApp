package com.osproject.microservices.fund.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class webClientConfig {

//    @Bean
//    public WebClient getWebClient(WebClient.Builder builder) {
//        return builder.build();
//    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
