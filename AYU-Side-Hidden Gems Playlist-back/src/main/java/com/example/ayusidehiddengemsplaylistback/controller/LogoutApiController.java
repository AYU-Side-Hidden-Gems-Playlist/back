package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.service.LogoutService;
import com.example.ayusidehiddengemsplaylistback.util.Utilities;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "kakaoLogout", description = "logout, refresh token 만료 처리 등 logout 위한 모든 행위")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LogoutApiController {

    private final LogoutService logoutService;

    @Tag(name = "kakaoLogout", description = "logout, refresh token 만료 처리 등 logout 위한 모든 행위")
    @Operation(summary = "kakaoLogout", description = "로그아웃시 로그아웃과 더불어, refresh token 만료 처리 기능을 수행합니다")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        Utilities.validateAuthorization(authorization);

        String accessToken = authorization.split(" ")[1];
        logoutService.logout(accessToken);

        return ResponseEntity.ok("logout Success");
    }
}