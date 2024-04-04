package com.example.ayusidehiddengemsplaylistback.service;


import com.example.ayusidehiddengemsplaylistback.domain.entity.TokenType;
import com.example.ayusidehiddengemsplaylistback.domain.entity.Member;
import com.example.ayusidehiddengemsplaylistback.error.ErrorCode;
import com.example.ayusidehiddengemsplaylistback.error.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class LogoutService {

    private final MemberService memberService;
    private final TokenManager tokenManager;

    public void logout(String accessToken) {
        //1. 토큰 검증
        tokenManager.validateToken(accessToken);
        //2. access_token 검증
        Claims claims = tokenManager.getTokenClaims(accessToken);
        String tokenType = claims.getSubject();
        if (!TokenType.isAccessToken(tokenType)) {
            throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }
        //3. 토큰 만료 처리
        Long memberId = Long.valueOf((Integer) claims.get("memberId"));
        Member member = memberService.findMemberByMemberId(memberId);
        member.expireRefreshToken(LocalDateTime.now());
    }
}