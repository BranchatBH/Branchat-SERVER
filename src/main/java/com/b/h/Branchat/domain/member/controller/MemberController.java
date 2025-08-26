package com.b.h.Branchat.domain.member.controller;

import static com.b.h.Branchat.domain.member.controller.message.MemberMessage.USER_INFO_RETRIEVED_SUCCESS;

import com.b.h.Branchat.domain.auth.service.AuthService;
import com.b.h.Branchat.domain.member.dto.response.MemberInfoResponse;
import com.b.h.Branchat.domain.member.service.MemberService;
import com.b.h.Branchat.global.dto.response.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<MemberInfoResponse, Void>> getUserInfo(
        Authentication authentication
    ) {
        UUID memberId = UUID.fromString(authentication.getName());
        log.info("Get user info for member id {}", memberId);
        MemberInfoResponse response = memberService.getUserInfo(memberId);
        log.info(response.toString());
        return ResponseEntity.ok(ApiResponse.ok(USER_INFO_RETRIEVED_SUCCESS, response));
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(Authentication authentication,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String accessToken = authorizationHeader.substring(7);
        UUID memberId = UUID.fromString(authentication.getName());

        authService.invalidateTokens(memberId, accessToken);

        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
