package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.entity.GrantType;
import com.example.ayusidehiddengemsplaylistback.entity.Member;
import com.example.ayusidehiddengemsplaylistback.entity.TokenType;
import com.example.ayusidehiddengemsplaylistback.exception.BusinessException;
import com.example.ayusidehiddengemsplaylistback.exception.ErrorCode;
import com.example.ayusidehiddengemsplaylistback.filter.TokenManager;
import com.example.ayusidehiddengemsplaylistback.form.TokenForm;
import com.example.ayusidehiddengemsplaylistback.form.kakaoLoginForm;
import com.example.ayusidehiddengemsplaylistback.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final TokenManager tokenManager;
    private final MemberRepository memberRepository;

    @Value("${kakao.client.id}")
    private String CLIENT_ID;

    @Value("${kakao.client.secret}")
    private String CLIENT_SECRET;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URI;

    @Value("${kakao.auth.url}")
    private String KAKAO_KAUTH_URI;

    @Value("${kakao.api.url}")
    private String KAKAO_API_URI;



    public String getKakaoLoginURL() {
        return KAKAO_KAUTH_URI + "/oauth/authorize"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URI
                + "&response_type=code";
    }

    /**
     * 카카오 서버로부터 회원 정보 불러오기
     */
    public String loginCallBack(String code) throws Exception {
        if (code == null) throw new Exception("Failed get authorization code");

        // form -> header parameter로 변경
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", KAKAO_REDIRECT_URI);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_KAUTH_URI + "/oauth/token",
                HttpMethod.POST,
                httpEntity,
                String.class);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

        String accessToken = (String) jsonObj.get("access_token");
        return accessToken;
    }

    /**
     * login
     */
    public TokenForm.JwtTokenForm oauthLogin(String accessToken) throws Exception {
        kakaoLoginForm memberInfo = getMemberInfo(accessToken);
//        log.info("[oauthLogin] memberInfo: {}", memberInfo); // 로그인한 회원정보를 잘 가져오는지 테스트를 원한다면 해당 라인을 디버깅 해주세요!!

        // 회원 구분
        Optional<Member> member = findMemberByEmail(memberInfo.getEmail());
        TokenForm.JwtTokenForm jwtTokenForm;
        Member oauthMember;
        if(member.isEmpty()) {
            oauthMember = kakaoLoginForm.toMemberEntity(memberInfo);
            registerMember(oauthMember);
        }
        else {
            oauthMember = member.get();
        }

        jwtTokenForm = tokenManager.generateJwtTokenFormByEmail(oauthMember.getEmail(), oauthMember.getRoles());
        oauthMember.updateRefreshToken(jwtTokenForm);

        return jwtTokenForm;
    }


    public kakaoLoginForm getMemberInfo(String accessToken) throws Exception { //KakaoLoginForm
        /** 헤더에 Bearer AccessToken을 담아 MemberInfo 요청 */
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        /** Response 데이터 파싱
         * Response 안에 있는 id 값은 회원 고유의 값입니다.
         */
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

        String email = String.valueOf(account.get("email"));
        String nickname = String.valueOf(profile.get("nickname"));
        String profileImage  = String.valueOf(profile.get("thumbnail_image_url"));

        return kakaoLoginForm.builder()
                .name(nickname)
                .email(email)
                .profile(profileImage)
                .build();
    }

    /**
     * AccessToken 재발급
     */
    public TokenForm.AccessTokenResponseForm generateAccessTokenByRefreshToken(String refreshToken) {
        Member member = findMemberByRefreshToken(refreshToken);

        Date accessTokenExpireTime = tokenManager.returnAccessTokenExpireTime(); //만료시간 설정

        String accessToken = tokenManager.generateAccessTokenByEmail(member.getEmail(), member.getRoles(), accessTokenExpireTime);
        return TokenForm.AccessTokenResponseForm.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .build();
    }

    /**
     * logout
     */
    public void logout(String accessToken) {
        //1. 토큰 검증
        tokenManager.validateToken(accessToken);

        //2. access_token 검증
        Claims claims = tokenManager.getTokenClaims(accessToken);
        String tokenType = claims.getSubject();
        if (!TokenType.isAccessToken(tokenType)) {
            throw new BusinessException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        //3. 토큰 만료 처리
        String email = (String) claims.get("email");
        Optional<Member> member = findMemberByEmail(email);
        member.get().expireRefreshToken(LocalDateTime.now());
    }


    /** MemberService
     * join, findMemberInfoByXXX
     */
    public void registerMember(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> m = memberRepository.findByEmail(member.getEmail());
        if (m.isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED_MEMBER);
        }
    }

    @Transactional(readOnly = true)
    public Member findMemberByRefreshToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken).
                orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        LocalDateTime tokenExpirationTime = member.getTokenExpirationTime();

        if (tokenExpirationTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        return member;
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

}