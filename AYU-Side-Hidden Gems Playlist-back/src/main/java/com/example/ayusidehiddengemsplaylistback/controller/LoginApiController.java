package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.domain.form.LoginForm;
import com.example.ayusidehiddengemsplaylistback.domain.form.TokenForm;
import com.example.ayusidehiddengemsplaylistback.service.LoginService;
import com.example.ayusidehiddengemsplaylistback.error.validator.OauthValidator;
import com.example.ayusidehiddengemsplaylistback.domain.entity.MemberType;
import com.example.ayusidehiddengemsplaylistback.util.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class LoginApiController {

    private final LoginService loginService;
    private final OauthValidator oauthValidator;

    /**
     * kakao 대한 컨트롤러
     */
//    @GetMapping("/oauth2/kakao")
//    public String loginCallBack(String code) {
//        String kakaoToken = loginService.loginCallBack(code);
//        return "kakao Token : " + kakaoToken;
//    }

    /**
     * 클라이언트로부터 소셜 로그인 요청을 받는 컨트롤러
     */
    @PostMapping("/api/oauth/login")
    public ResponseEntity<LoginForm.Response> oauthLogin(@RequestBody LoginForm.Request requestDto,
                                                         HttpServletRequest httpServletRequest) {
        /** 헤더에 있는 액세스 토큰 정보를 통해 카카오로부터 회원 정보를 가지고 온다. */
        String authorization = httpServletRequest.getHeader("Authorization");
        Utilities.validateAuthorization(authorization);
        oauthValidator.validateMemberType(requestDto.getMemberType());
        String accessToken = authorization.split(" ")[1];   //Bearer+" "+accessToken

        LoginForm.Response jwtTokenResponseForm = loginService.oauthLogin(accessToken, MemberType.from(requestDto.getMemberType()));

        return ResponseEntity.ok(jwtTokenResponseForm);
    }

    /**
     * 토큰 재발급에 대한 처리를 위한 컨트롤러
     */
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
