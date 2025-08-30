package com.b.h.Branchat.infra.client.oauth.google.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.google")
public record GoogleOauthProperties(
    String clientId,
    String clientSecret,
    String tokenUri,
    String redirectUri,
    String userInfoUri,
    String grantType
) {}
