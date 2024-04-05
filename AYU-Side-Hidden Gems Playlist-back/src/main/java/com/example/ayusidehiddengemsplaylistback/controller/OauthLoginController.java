package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.dto.OauthLoginDto;
import com.example.ayusidehiddengemsplaylistback.service.OauthLoginService;
import com.example.ayusidehiddengemsplaylistback.error.validator.OauthValidator;
import com.example.ayusidehiddengemsplaylistback.domain.MemberType;
import com.example.ayusidehiddengemsplaylistback.util.AuthorizationHeaderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OauthLoginController {

    /**
     * 클라이언트로부터 소셜 로그인 요청을 받는 컨트롤러
     */

    private final OauthLoginService oauthLoginService;
    private final OauthValidator oauthValidator;

    @PostMapping("/login")
    public ResponseEntity<OauthLoginDto.Response> oauthLogin(@RequestBody OauthLoginDto.Request requestDto,
                                                             HttpServletRequest httpServletRequest) {
        /** 헤더에 있는 액세스 토큰 정보를 통해 카카오로부터 회원 정보를 가지고 온다. */
        String authorization = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorization);
        oauthValidator.validateMemberType(requestDto.getMemberType());
        String accessToken = authorization.split(" ")[1];   //Bearer+" "+accessToken

        OauthLoginDto.Response jwtTokenResponseDto = oauthLoginService.oauthLogin(accessToken, MemberType.from(requestDto.getMemberType()));

        return ResponseEntity.ok(jwtTokenResponseDto);
    }
}
