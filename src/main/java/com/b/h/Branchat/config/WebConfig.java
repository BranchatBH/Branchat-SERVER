package com.b.h.Branchat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class WebConfig {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        // 클라이언트의 요청 주소(URL)와 쿼리 문자열을 로그에 포함합니다.
        filter.setIncludeQueryString(true);
        // 요청 본문(payload/body)을 로그에 포함합니다. (중요!)
        filter.setIncludePayload(true);
        // 로그에 담을 수 있는 최대 본문 길이를 설정합니다. (예: 10000자)
        filter.setMaxPayloadLength(10000);
        // 요청 헤더 정보를 로그에 포함합니다.
        filter.setIncludeHeaders(true);
        // 요청 처리 전과 후의 메시지를 모두 출력하도록 설정합니다.
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
}
