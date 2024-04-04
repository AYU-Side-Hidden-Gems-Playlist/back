package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/oauth2/kakao")
    public @ResponseBody String loginCallBack(String code) {
        String kakaoToken = loginService.loginCallBack(code);
        return "kakao Token : " + kakaoToken;
    }

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

}
