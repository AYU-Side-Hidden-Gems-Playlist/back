package com.example.ayusidehiddengemsplaylistback.service.social.kakao;

import com.example.ayusidehiddengemsplaylistback.configuration.GrantType;
import com.example.ayusidehiddengemsplaylistback.domain.MemberType;
import com.example.ayusidehiddengemsplaylistback.dto.OAuthAttributes;
import com.example.ayusidehiddengemsplaylistback.client.KakaoMemberInfoClient;
import com.example.ayusidehiddengemsplaylistback.dto.KakaoMemberInfoResponseDto;
import com.example.ayusidehiddengemsplaylistback.service.social.SocialLoginApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 소셜 로그인 API 서비스 인터페이스 구현체
 * */
@Slf4j @Service
@RequiredArgsConstructor
@Transactional
public class KakaoLoginApiServiceImpl implements SocialLoginApiService {

    private final KakaoMemberInfoClient kakaoMemberInfoClient;
    private final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";

    @Override
    public OAuthAttributes getMemberInfo(String accessToken) {
        /** 회원정보 가져오기 */
        KakaoMemberInfoResponseDto kakaoMemberInfo = kakaoMemberInfoClient.getKakaoMemberInfo(
                CONTENT_TYPE,
                GrantType.BEARER.getType() + " " + accessToken);
        KakaoMemberInfoResponseDto.KakaoAccount kakaoAccount = kakaoMemberInfo.getKakaoAccount();
        String email = kakaoAccount.getEmail();

        /** 검증 받지 않은 경우, 이메일을 필수 값으로 설정할 수 없음
         * 해당 경우에는 임시로 아이디 저장 */
        return OAuthAttributes.builder()
                .name(kakaoAccount.getProfile().getNickname())
                .email(!StringUtils.hasText(email) ? kakaoMemberInfo.getId() : email) //
                .profile(kakaoAccount.getProfile().getThumbnailImageUrl())
                .memberType(MemberType.kakao)
                .build();
    }

}
