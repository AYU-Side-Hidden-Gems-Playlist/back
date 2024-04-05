package com.example.ayusidehiddengemsplaylistback.domain.form;

import com.example.ayusidehiddengemsplaylistback.domain.entity.Member;
import com.example.ayusidehiddengemsplaylistback.domain.entity.MemberRole;
import com.example.ayusidehiddengemsplaylistback.domain.entity.MemberType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter @Builder
public class JoinForm {
    /**
     * OAuthAttributes
     * 소셜 플랫폼에서 가져오는 회원정보 통일을 위한 클래스로, 해당 클래스로 회원가입을 진행한다.
     */

    private String name;
    private String email;
    private String profile;
    private MemberType memberType;
    private MemberRole role;

    public Member toMemberEntity(MemberType memberType, MemberRole role) {
        return Member.builder()
                .name(name)
                .email(email)
                .memberType(memberType)
                .profile(profile)
                .role(role)
                .build();
    }
}
