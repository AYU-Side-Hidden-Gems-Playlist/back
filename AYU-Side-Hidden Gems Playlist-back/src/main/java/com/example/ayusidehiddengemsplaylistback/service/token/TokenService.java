package com.example.ayusidehiddengemsplaylistback.service.token;

import com.example.ayusidehiddengemsplaylistback.configuration.GrantType;
import com.example.ayusidehiddengemsplaylistback.domain.Member;
import com.example.ayusidehiddengemsplaylistback.dto.token.AccessTokenResponseDto;
import com.example.ayusidehiddengemsplaylistback.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final MemberService memberService;
    private final TokenManager tokenManager;

    public AccessTokenResponseDto generateAccessTokenByRefreshToken(String refreshToken) {
        Member member = memberService.findMemberByRefreshToken(refreshToken);

        Date accessTokenExpireTime = tokenManager.returnAccessTokenExpireTime(); //만료시간 설정
        String accessToken = tokenManager.generateAccessToken(member.getMemberId(), member.getRole(), accessTokenExpireTime);

        return AccessTokenResponseDto.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .build();
    }
}