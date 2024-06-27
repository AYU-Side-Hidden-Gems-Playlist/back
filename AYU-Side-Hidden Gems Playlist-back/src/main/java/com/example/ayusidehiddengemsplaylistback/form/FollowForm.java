package com.example.ayusidehiddengemsplaylistback.form;

import com.example.ayusidehiddengemsplaylistback.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.ayusidehiddengemsplaylistback.form.MemberForm.toMemberForm;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowForm {

    private Long followId;
    private MemberForm follower;
    private MemberForm following;

    public static FollowForm toFollowForm(Follow follow) {
        return FollowForm.builder()
                .followId(follow.getFollowId())
                .follower(toMemberForm(follow.getFollower()))
                .following(toMemberForm(follow.getFollowing()))
                .build();
    }

}
