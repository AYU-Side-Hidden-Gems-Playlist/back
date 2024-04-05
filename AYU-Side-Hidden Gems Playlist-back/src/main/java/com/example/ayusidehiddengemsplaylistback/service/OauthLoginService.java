package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.dto.OauthLoginDto;
import com.example.ayusidehiddengemsplaylistback.dto.token.JwtTokenDto;
import com.example.ayusidehiddengemsplaylistback.domain.Member;
import com.example.ayusidehiddengemsplaylistback.domain.MemberRole;
import com.example.ayusidehiddengemsplaylistback.domain.MemberType;
import com.example.ayusidehiddengemsplaylistback.dto.OAuthAttributes;
import com.example.ayusidehiddengemsplaylistback.service.social.SocialLoginApiService;
import com.example.ayusidehiddengemsplaylistback.service.social.SocialLoginApiServiceFactory;
import com.example.ayusidehiddengemsplaylistback.service.token.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional
public class OauthLoginService {

    /**
     * 카카오 서버로부터 회원 정보 불러오기
     */

    private final MemberService memberService;
    private final TokenManager tokenManager;


    public OauthLoginDto.Response oauthLogin(String accessToken, MemberType memberType) {
        /**
         * 소셜 회원 정보 가져오기 (사용할 소셜 로그인 API 서비스)
         */
        SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(memberType); //kakaoLoginServiceImpl 반환받게 됨
        OAuthAttributes memberInfo = socialLoginApiService.getMemberInfo(accessToken); //회원가입 처리 할 회원정보
        log.info("memberInfo: {}", memberInfo); // 로그인한 회원정보를 잘 가져오는지 테스트를 원한다면 해당 라인을 디버깅 해주세요!!

        // 신규/기존 회원 구분
        Optional<Member> member = memberService.findMemberByEmail(memberInfo.getEmail());
        JwtTokenDto jwtTokenDto;

        if(member.isEmpty()) {
            Member oauthMember = memberInfo.toMemberEntity(memberType, MemberRole.ADMIN); //1. 회원생성
            memberService.registerMember(oauthMember);  //2. 회원가입
            //3. 토큰생성
            jwtTokenDto = tokenManager.generateJwtTokenDto(oauthMember.getMemberId(), oauthMember.getRole());
            //4. 리프레시 토큰정보와 토큰만료 기간또한 업데이트 해주어야 함!(등록)
            oauthMember.updateRefreshToken(jwtTokenDto);
        }
        else {
            Member oauthMember = member.get();
            // 기존 회원이므로 바로 토큰 생성
            jwtTokenDto = tokenManager.generateJwtTokenDto(oauthMember.getMemberId(), oauthMember.getRole());
            // refrestToken & tokenExpirationTime 업데이트
            oauthMember.updateRefreshToken(jwtTokenDto);
        }

        return OauthLoginDto.Response.of(jwtTokenDto);
    }

}