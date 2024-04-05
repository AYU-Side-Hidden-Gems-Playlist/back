package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.dto.token.AccessTokenResponseDto;
import com.example.ayusidehiddengemsplaylistback.service.token.TokenService;
import com.example.ayusidehiddengemsplaylistback.util.AuthorizationHeaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {

    /**
     * 토큰 재발급에 대한 처리를 위한 컨트롤러
     */

    private final TokenService tokenService;

    @PostMapping("/access-token/issue")
    public ResponseEntity<AccessTokenResponseDto> generateAccessToken(HttpServletRequest httpServletRequest) {

        /** authorization header에 담긴 refresh token을 꺼내기 위해 HttpServletRequest를 파라미터로 받는다. */
        String authorization = httpServletRequest.getHeader("Authorization");

        AuthorizationHeaderUtils.validateAuthorization(authorization);

        String refreshToken = authorization.split(" ")[1];
        AccessTokenResponseDto dto = tokenService.generateAccessTokenByRefreshToken(refreshToken);

        return ResponseEntity.ok(dto);
    }

}