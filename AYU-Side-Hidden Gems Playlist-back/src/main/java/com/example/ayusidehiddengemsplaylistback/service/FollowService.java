package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.entity.Follow;
import com.example.ayusidehiddengemsplaylistback.entity.Member;
import com.example.ayusidehiddengemsplaylistback.exception.BusinessException;
import com.example.ayusidehiddengemsplaylistback.exception.ErrorCode;
import com.example.ayusidehiddengemsplaylistback.form.FollowForm;
import com.example.ayusidehiddengemsplaylistback.form.MemberForm;
import com.example.ayusidehiddengemsplaylistback.repository.FollowRepository;
import com.example.ayusidehiddengemsplaylistback.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    /**
     * 팔로우
     * A가 B를 팔로우한다.
     * A가 B의 팔로워, B는 A의 팔로잉.
     * A의 following +1
     * B의 follower +1
     */
    public void follow(Long followerId, Long followingId) {
        Member follower = memberRepository.findByMemberId(followerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLLOWER_NOT_FOUND));
        Member following = memberRepository.findByMemberId(followingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLLOWING_NOT_FOUND));

        if (followRepository.findByFollowerAndFollowing(follower, following).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED_FOLLWING);
        }

        followRepository.save(
                Follow.builder()
                        .follower(follower)
                        .following(following)
                        .build()
        );
    }

    /**
     * 팔로우 취소
     */
    public void unfollow(Long followerId, Long followingId) {
        Member follower = memberRepository.findByMemberId(followerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLLOWER_NOT_FOUND));
        Member following = memberRepository.findByMemberId(followingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLLOWING_NOT_FOUND));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOLLWING));

        followRepository.delete(follow);
    }

    public List<MemberForm> getFollowers(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return followRepository.findByFollowing(member).stream()
                .map(follow -> MemberForm.toMemberForm(follow.getFollower()))
                .collect(Collectors.toList());
    }

    public List<MemberForm> getFollowings(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return followRepository.findByFollower(member).stream()
                .map(follow -> MemberForm.toMemberForm(follow.getFollowing()))
                .collect(Collectors.toList());
    }

}
