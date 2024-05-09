package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.entity.Member;
import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.repository.MemberRepository;
import com.example.ayusidehiddengemsplaylistback.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PlaylistRepository playlistRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, PlaylistRepository playlistRepository) {
        this.memberRepository = memberRepository;
        this.playlistRepository = playlistRepository;
    }


    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public List<Playlist> findPlaylistsByMemberId(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (!memberOptional.isPresent()) {
            // 회원이 존재하지 않을 때, 예외 처리 혹은 빈 리스트 반환
            throw new EntityNotFoundException("Member not found with id: " + memberId);
            // 또는 return Collections.emptyList();
        }
        Member member = memberOptional.get();
        return playlistRepository.findByMember(member);
    }

}
