package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.domain.form.TokenForm;
import com.example.ayusidehiddengemsplaylistback.service.LoginService;
import com.example.ayusidehiddengemsplaylistback.util.Utilities;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "kakaoLogin/Logout", description = "login, logout를 위한 모든 행위")
@RestController @Slf4j
@RequiredArgsConstructor
public class LoginApiController {

    private final LoginService loginService;

    /**
     * 클라이언트로부터 소셜 로그인 요청을 받는 컨트롤러
     */
    @GetMapping("/oauth2/kakao")
    public ResponseEntity<Object> loginCallBack(HttpServletRequest httpServletRequest) throws Exception {
        log.info("httpServletRequest.getParameter(\"code\"): {}", httpServletRequest.getParameter("code"));
        String callBack = loginService.loginCallBack(httpServletRequest.getParameter("code"));

        String authorization = httpServletRequest.getHeader("Authorization");
        TokenForm.JwtTokenForm jwtToken = loginService.oauthLogin(callBack);

        return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);//jwtoken 반환
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

    @Tag(name = "kakaoLogout")
    @Operation(summary = "kakaoLogout", description = "로그아웃시 로그아웃과 더불어, refresh token 만료 처리 기능을 수행합니다")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        Utilities.validateAuthorization(authorization);

        String accessToken = authorization.split(" ")[1];
        loginService.logout(accessToken);

        return ResponseEntity.ok("logout Success");
    }

    // (주석: 추후 view와 연결될 컨트롤러)
//    @Tag(name = "kakaoLogin")
//    @Operation(summary = "kakaoLogin", description = "token으로 회원가입 및 로그인 기능을 수행합니다")
//    @PostMapping("/api/oauth/login")
//    public ResponseEntity<String> oauthLogin(HttpServletRequest httpServletRequest) throws Exception {
//        /** 헤더에 있는 액세스 토큰 정보를 통해 카카오로부터 회원 정보를 가지고 온다. */
//        String authorization = httpServletRequest.getHeader("Authorization");
//        Utilities.validateAuthorization(authorization);
//        String accessToken = authorization.split(" ")[1];   //Bearer+" "+accessToken
//
//        TokenForm.JwtTokenForm jwtToken = loginService.oauthLogin(accessToken);
//
//        return ResponseEntity.ok(accessToken);
//    }
//
//    @GetMapping("/api/oauth/login")
//    public ResponseEntity<String> login(Model model) {
//        model.addAttribute("kakaoURL", loginService.getKakaoLoginURL());
//        return ResponseEntity.ok(model.toString());
//    }
}
