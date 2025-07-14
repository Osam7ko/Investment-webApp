package com.osproject.microservices.fund.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class webClientConfig {

    @Bean
    public WebClient getWebClient(WebClient.Builder builder) {
        return builder.build();
    }
}
