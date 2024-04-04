package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.domain.form.LoginForm;
import com.example.ayusidehiddengemsplaylistback.domain.form.TokenForm;
import com.example.ayusidehiddengemsplaylistback.error.ErrorCode;
import com.example.ayusidehiddengemsplaylistback.error.exception.AuthenticationException;
import com.example.ayusidehiddengemsplaylistback.service.LoginService;
import com.example.ayusidehiddengemsplaylistback.domain.entity.MemberType;
import com.example.ayusidehiddengemsplaylistback.util.Utilities;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Tag(name = "kakaoLogin", description = "login, login을 위한 토큰 재발급 등 login을 위한 모든 행위")
@RestController
@RequiredArgsConstructor
public class LoginApiController {

    private final LoginService loginService;

    /**
     * 클라이언트로부터 소셜 로그인 요청을 받는 컨트롤러
     */
    @Tag(name = "kakaoLogin")
    @Operation(summary = "kakaoLogin", description = "token으로 회원가입 및 로그인 기능을 수행합니다")
    @PostMapping("/api/oauth/login")
    public ResponseEntity<LoginForm.Response> oauthLogin(@RequestBody LoginForm.Request requestDto,
                                                         HttpServletRequest httpServletRequest) {
        /** 헤더에 있는 액세스 토큰 정보를 통해 카카오로부터 회원 정보를 가지고 온다. */
        String authorization = httpServletRequest.getHeader("Authorization");
        Utilities.validateAuthorization(authorization);
        validateMemberType(requestDto.getMemberType());
        String accessToken = authorization.split(" ")[1];   //Bearer+" "+accessToken

        LoginForm.Response jwtTokenResponseForm = loginService.oauthLogin(accessToken, MemberType.from(requestDto.getMemberType()));

        return ResponseEntity.ok(jwtTokenResponseForm);
    }
    public void validateMemberType(String memberType) {
        if(!MemberType.isMemberType(memberType))
            throw new AuthenticationException(ErrorCode.INVALID_MEMBER_TYPE);
    }

    /**
     * 토큰 재발급에 대한 처리를 위한 컨트롤러
     */
    @Tag(name = "kakaoLogin")
    @Operation(summary = "kakaoLogin 토큰재발급", description = "refrest token을 이용하여 토큰 재발급 기능을 수행합니다")
    @PostMapping("/api/access-token/issue")
    public ResponseEntity<TokenForm.AccessTokenResponseForm> generateAccessToken(HttpServletRequest httpServletRequest) {
        /** authorization header에 담긴 refresh token을 꺼내기 위해 HttpServletRequest를 파라미터로 받는다. */
        String authorization = httpServletRequest.getHeader("Authorization");

        Utilities.validateAuthorization(authorization);

        String refreshToken = authorization.split(" ")[1];
        TokenForm.AccessTokenResponseForm form = loginService.generateAccessTokenByRefreshToken(refreshToken);

        return ResponseEntity.ok(form);
    }

}
