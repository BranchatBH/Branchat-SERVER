package com.b.h.Branchat.domain.auth.entity;

import org.springframework.data.annotation.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60*60*24*14)
public class HashedRefreshToken {
    @Id
    private String hashedRefreshToken;

    @Indexed
    private String memberId;

    //리프레시 토큰의 생명 주기(14일)
    @TimeToLive
    private Long ttl;

    @Builder
    public HashedRefreshToken(String hashedRefreshToken, String memberId) {
        this.hashedRefreshToken = hashedRefreshToken;
        this.memberId = memberId;
        this.ttl = 60L * 60 * 24 * 14;
    }
}
