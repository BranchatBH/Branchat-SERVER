package com.b.h.Branchat.domain.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthProviderRepositoryImpl implements AuthProviderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}
