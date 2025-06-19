package org.luvin.codiblyserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient openMeteoWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.open-meteo.com/v1")
                .build();
    }
}
