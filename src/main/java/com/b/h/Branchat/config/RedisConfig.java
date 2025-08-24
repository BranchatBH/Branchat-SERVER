package com.b.h.Branchat.config;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    // Redis 비밀번호를 주입받습니다. 설정되어 있지 않다면 빈 문자열("")이 기본값으로 사용됩니다.
    @Value("${spring.data.redis.password:}")
    private String password;

    // Redis 연결에 SSL/TLS를 사용할지 여부를 주입받습니다. 기본값은 false입니다.
    @Value("${spring.data.redis.ssl:false}")
    private boolean ssl;

    // Redis 명령어 실행 타임아웃을 주입받습니다. 기본값은 1초입니다.
    @Value("${spring.data.redis.timeout:1s}")
    private Duration commandTimeout;

    /* Use Lettuce for Redis (Jedis has been deprecated since Spring Boot 2.0) */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 호스트와 포트를 사용하여 RedisStandaloneConfiguration을 초기화합니다.
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);

        // 비밀번호가 설정되어 있고 비어 있지 않다면, redisConfig에 비밀번호를 설정합니다.
        if (password != null && !password.isEmpty()) {
            redisConfig.setPassword(password);
        }

        // LettuceClientConfiguration 빌더를 생성하여 클라이언트 설정을 정의합니다.
        var clientBuilder = LettuceClientConfiguration.builder()
            // 명령어 타임아웃을 주입받은 commandTimeout으로 설정합니다.
            .commandTimeout(commandTimeout)
            // 애플리케이션 종료 시 Redis 클라이언트 연결을 즉시 종료하도록 설정합니다.
            .shutdownTimeout(Duration.ZERO);

        // SSL 사용이 true로 설정되어 있다면, SSL을 활성화합니다.
        if (ssl) {
            clientBuilder.useSsl();
        }

        // 최종 LettuceClientConfiguration 객체를 빌드합니다.
        LettuceClientConfiguration clientConfig = clientBuilder.build();

        // Redis 연결 설정을 로그로 출력합니다.
        log.info("Configured Redis at {}:{} (ssl:{}, timeout:{})", host, port, ssl, commandTimeout);
        // 설정된 RedisStandaloneConfiguration과 LettuceClientConfiguration으로 LettuceConnectionFactory를 생성하여 반환합니다.
        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
