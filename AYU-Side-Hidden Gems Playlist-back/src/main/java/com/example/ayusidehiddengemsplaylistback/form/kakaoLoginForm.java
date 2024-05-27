package com.example.ayusidehiddengemsplaylistback.form;

import com.example.ayusidehiddengemsplaylistback.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class kakaoLoginForm {
    /**
     * OAuthAttributes
     * 소셜 플랫폼에서 가져오는 회원정보 통일을 위한 클래스로, 해당 클래스로 회원가입을 진행한다.
     */

    private String name;
    private String email;
    private String profile;

    public static Member toMemberEntity(kakaoLoginForm kakaoLoginForm) {
        return Member.builder()
                .name(kakaoLoginForm.getName())
                .email(kakaoLoginForm.getEmail())
                .profile(kakaoLoginForm.getProfile())
                .build();
    }
}
