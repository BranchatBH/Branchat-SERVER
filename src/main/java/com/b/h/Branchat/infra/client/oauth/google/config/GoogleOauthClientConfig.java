package com.b.h.Branchat.infra.client.oauth.google.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(GoogleOauthProperties.class)
public class GoogleOauthClientConfig {

    @Bean(name = "authRestClient")
    public RestClient authRestClient(SimpleClientHttpRequestFactory factory) {
        return RestClient.builder()
            .requestFactory(factory)
            .build();
    }
}
