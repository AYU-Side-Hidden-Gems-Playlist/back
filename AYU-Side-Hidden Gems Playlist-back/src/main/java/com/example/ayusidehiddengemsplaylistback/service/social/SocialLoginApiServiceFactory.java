package com.example.ayusidehiddengemsplaylistback.service.social;

import com.example.ayusidehiddengemsplaylistback.domain.MemberType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SocialLoginApiServiceFactory {
    private static Map<String, SocialLoginApiService> socialLoginApiServices; //소셜 로그인 API 서비스 구현체

    public SocialLoginApiServiceFactory(Map<String, SocialLoginApiService> socialLoginApiServices) {
        this.socialLoginApiServices = socialLoginApiServices;
    }

    /** 소셜 로그인 확장을 고려 */
    public static SocialLoginApiService getSocialLoginApiService(MemberType memberType) {
        String socialLoginApiServiceBeanName = "";

        if(MemberType.kakao.equals(memberType))
            socialLoginApiServiceBeanName = "kakaoLoginApiServiceImpl";

        //해당 빈 이름을 Map에서 꺼내 반환
        return socialLoginApiServices.get(socialLoginApiServiceBeanName);
    }

}