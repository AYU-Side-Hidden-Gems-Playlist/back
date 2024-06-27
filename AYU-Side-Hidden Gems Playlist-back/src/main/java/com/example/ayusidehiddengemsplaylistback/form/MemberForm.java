package com.example.ayusidehiddengemsplaylistback.form;

import com.example.ayusidehiddengemsplaylistback.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberForm {

    private Long memberId;
    private String email;
    private String name;
    private String profile;

    public static MemberForm toMemberForm(Member member) {
        return MemberForm.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .name(member.getName())
                .profile(member.getProfile())
                .build();
    }
}
