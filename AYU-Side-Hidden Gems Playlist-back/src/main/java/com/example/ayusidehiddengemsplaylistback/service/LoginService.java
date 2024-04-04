package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.client.KakaoMemberInfoClient;
import com.example.ayusidehiddengemsplaylistback.client.KakaoTokenClient;
import com.example.ayusidehiddengemsplaylistback.domain.entity.GrantType;
import com.example.ayusidehiddengemsplaylistback.domain.form.JoinForm;
import com.example.ayusidehiddengemsplaylistback.domain.form.LoginForm;
import com.example.ayusidehiddengemsplaylistback.domain.entity.Member;
import com.example.ayusidehiddengemsplaylistback.domain.entity.MemberRole;
import com.example.ayusidehiddengemsplaylistback.domain.entity.MemberType;
import com.example.ayusidehiddengemsplaylistback.domain.form.KakaoForm;
import com.example.ayusidehiddengemsplaylistback.domain.form.TokenForm;
import com.example.ayusidehiddengemsplaylistback.domain.form.KakaoForm.MemberInfoResponse;
import com.example.ayusidehiddengemsplaylistback.error.ErrorCode;
import com.example.ayusidehiddengemsplaylistback.error.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional
public class LoginService {

    /**
     * 카카오 서버로부터 회원 정보 불러오기
     */

    private final MemberService memberService;
    private final TokenManager tokenManager;
    private final KakaoTokenClient kakaoTokenClient;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    private final KakaoMemberInfoClient kakaoMemberInfoClient;
    private final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";



    public String loginCallBack(String code) {
        String contentType = "application/x-www-form-urlencoded;charset=utf-8";
        KakaoForm.Request kakaoTokenRequestForm = KakaoForm.Request.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(code)
                .redirect_uri("http://localhost:8080/oauth2/kakao")
                .build();
        KakaoForm.Response kakaoToken = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestForm);

        return kakaoToken.toString();
    }

    public LoginForm.Response oauthLogin(String accessToken, MemberType memberType) {
        if(!memberType.toString().equals("kakao"))
            throw new AuthenticationException(ErrorCode.INVALID_MEMBER_TYPE);

        JoinForm memberInfo = getMemberInfo(accessToken);
        log.info("memberInfo: {}", memberInfo); // 로그인한 회원정보를 잘 가져오는지 테스트를 원한다면 해당 라인을 디버깅 해주세요!!

        // 회원 구분
        Optional<Member> member = memberService.findMemberByEmail(memberInfo.getEmail());
        TokenForm.JwtTokenForm jwtTokenForm;
        Member oauthMember;

        if(member.isEmpty()) {
            oauthMember = memberInfo.toMemberEntity(memberType, MemberRole.ADMIN);
            memberService.registerMember(oauthMember);
        }
        else {
            oauthMember = member.get();
        }
        jwtTokenForm = tokenManager.generateJwtTokenForm(oauthMember.getMemberId(), oauthMember.getRole());
        oauthMember.updateRefreshToken(jwtTokenForm);

        return LoginForm.Response.of(jwtTokenForm);
    }

    public JoinForm getMemberInfo(String accessToken) {
        MemberInfoResponse kakaoMemberInfo = kakaoMemberInfoClient.getKakaoMemberInfo(
                CONTENT_TYPE,
                GrantType.BEARER.getType() + " " + accessToken);
        MemberInfoResponse.KakaoAccount kakaoAccount = kakaoMemberInfo.getKakaoAccount();
        String email = kakaoAccount.getEmail();

        return JoinForm.builder()
                .name(kakaoAccount.getProfile().getNickname())
                .email(!StringUtils.hasText(email) ? kakaoMemberInfo.getId() : email) //
                .profile(kakaoAccount.getProfile().getThumbnailImageUrl())
                .memberType(MemberType.kakao)
                .build();
    }

    /**
     * AccessToken 재발급
     */
    public TokenForm.AccessTokenResponseForm generateAccessTokenByRefreshToken(String refreshToken) {
        Member member = memberService.findMemberByRefreshToken(refreshToken);

        Date accessTokenExpireTime = tokenManager.returnAccessTokenExpireTime(); //만료시간 설정
        String accessToken = tokenManager.generateAccessToken(member.getMemberId(), member.getRole(), accessTokenExpireTime);

        return TokenForm.AccessTokenResponseForm.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .build();
    }

}