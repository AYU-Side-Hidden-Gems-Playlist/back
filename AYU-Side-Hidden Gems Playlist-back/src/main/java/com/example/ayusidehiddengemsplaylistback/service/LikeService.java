package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.entity.Member;
import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.entity.Like;
import com.example.ayusidehiddengemsplaylistback.form.LikeForm;
import com.example.ayusidehiddengemsplaylistback.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ayusidehiddengemsplaylistback.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PlaylistRepository playlistRepository;

    @Transactional
    public void like(LikeForm likeForm) throws Exception {
        Member member = memberRepository.findById(likeForm.getMemberId())
                .orElseThrow(() -> new NotFoundException("Could not found member id : " + likeForm.getMemberId())); //notfoundexception클래스 추가예정

        Playlist playlist = playlistRepository.findById(likeForm.getPlaylistId())
                .orElseThrow(() -> new NotFoundException("Could not found playlist id : " + likeForm.getPlaylistId())); //동일

        if (likeRepository.findByMemberAndPlaylist(member, playlist).isPresent()){
            throw new Exception("Like already exists.");
        }

        Like like = Like.builder()
                .playlist(playlist)
                .member(member)
                .build();

        likeRepository.save(like);
    }

    @Transactional
    public void unlike(LikeForm likeForm) {
        Member member = memberRepository.findById(likeForm.getMemberId())
                .orElseThrow(() -> new NotFoundException("Miss member id : " + likeForm.getMemberId()));

        Playlist playlist = playlistRepository.findById(likeForm.getPlaylistId())
                .orElseThrow(() -> new NotFoundException("Miss playlist id : " + likeForm.getPlaylistId()));

        Like like = likeRepository.findByMemberAndPlaylist(member, playlist)
                .orElseThrow(() -> new NotFoundException("Miss Like Id"));

        likeRepository.delete(like);
    }
}
