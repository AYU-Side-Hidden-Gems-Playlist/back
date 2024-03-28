package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.domain.Member;
import com.example.ayusidehiddengemsplaylistback.error.ErrorCode;
import com.example.ayusidehiddengemsplaylistback.error.exception.AuthenticationException;
import com.example.ayusidehiddengemsplaylistback.error.exception.BusinessException;
import com.example.ayusidehiddengemsplaylistback.error.exception.EntityNotFoundException;
import com.example.ayusidehiddengemsplaylistback.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */

    public void registerMember(Member member) {
        validateDupliateMember(member);
        memberRepository.save(member);
    }

    private void validateDupliateMember(Member member) {
        Optional<Member> m = memberRepository.findByEmail(member.getEmail());
        if (m.isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED_MEMBER);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Member findMemberByRefreshToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken).
                orElseThrow(() -> new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        LocalDateTime tokenExpirationTime = member.getTokenExpirationTime();

        if (tokenExpirationTime.isBefore(LocalDateTime.now())) {
            throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        return member;
    }

    public Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).
                orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_EXISTS));
    }
}