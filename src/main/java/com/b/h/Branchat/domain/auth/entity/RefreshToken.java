package com.b.h.Branchat.domain.auth.entity;

import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60*60*24*14)
public class RefreshToken {
    @Id
    @Indexed
    private String jwtRefreshToken;

    // 맴버 이메일로 설정
    private String memberId;

    //리프레시 토큰의 생명 주기(14일)
    @TimeToLive
    private Long ttl;

    @Builder
    public RefreshToken(String jwtRefreshToken, String memberId) {
        this.jwtRefreshToken = jwtRefreshToken;
        this.memberId = memberId;
        this.ttl = 1000L * 60 * 60 * 24 * 14;
    }
}
