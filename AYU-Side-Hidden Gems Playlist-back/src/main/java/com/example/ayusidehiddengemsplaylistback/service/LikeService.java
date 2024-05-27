package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.entity.Like;
import com.example.ayusidehiddengemsplaylistback.entity.Member;
import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.exception.BusinessException;
import com.example.ayusidehiddengemsplaylistback.exception.ErrorCode;
import com.example.ayusidehiddengemsplaylistback.form.LikeForm;
import com.example.ayusidehiddengemsplaylistback.repository.LikeRepository;
import com.example.ayusidehiddengemsplaylistback.repository.MemberRepository;
import com.example.ayusidehiddengemsplaylistback.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PlaylistRepository playlistRepository;

    @Transactional
    public void like(LikeForm likeForm) throws Exception {
        Member member = memberRepository.findById(likeForm.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_MEMBER_NOT_FOUND));
        Playlist playlist = playlistRepository.findById(likeForm.getPlaylistId())
                .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_PLAYLIST_NOT_FOUND));

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
                .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_MEMBER_NOT_FOUND));
        Playlist playlist = playlistRepository.findById(likeForm.getPlaylistId())
                .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_PLAYLIST_NOT_FOUND));

        Like like = likeRepository.findByMemberAndPlaylist(member, playlist)
                .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_LIKE_NOT_FOUND));

        likeRepository.delete(like);
    }
}
