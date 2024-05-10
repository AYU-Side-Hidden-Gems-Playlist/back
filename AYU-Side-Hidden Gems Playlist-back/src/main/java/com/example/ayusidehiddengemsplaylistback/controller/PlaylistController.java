package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.entity.Member;
import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.entity.Song;
import com.example.ayusidehiddengemsplaylistback.form.PlaylistForm;
import com.example.ayusidehiddengemsplaylistback.form.SongForm;
import com.example.ayusidehiddengemsplaylistback.service.MemberService;
import com.example.ayusidehiddengemsplaylistback.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<Playlist> createPlaylist(@RequestParam String email, @Valid @RequestBody PlaylistForm playlistForm) {
        // 현재 인증된 사용자의 이메일 정보를 사용하여 Member 객체를 검색
        Optional<Member> member = memberService.findMemberByEmail(email);

        if (member == null) {
            // 만약 Member 가 데이터베이스에서 찾을 수 없으면 적절한 에러 처리가 필요
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Playlist playlist = new Playlist();
        playlist.setPlaylistTitle(playlistForm.getPlaylistTitle());
        playlist.setMember(member);  // 이메일을 통해 검색된 Member 객체 설정
        Playlist createdPlaylist = playlistService.createPlaylist(playlist);
        return ResponseEntity.ok(createdPlaylist);
    }

    @GetMapping("/read/{playlistId}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable Long playlistId) {
        Playlist playlist = playlistService.findPlaylistById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with playlistId " + playlistId));
        return ResponseEntity.ok(playlist);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update/{playlistId}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Long playlistId, @Valid @RequestBody PlaylistForm playlistForm) {
        Playlist playlistDetails = new Playlist();
        playlistDetails.setPlaylistTitle(playlistForm.getPlaylistTitle());
        Playlist updatedPlaylist = playlistService.updatePlaylist(playlistId, playlistDetails);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{playlistId}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.ok().build();
    }

    // 노래 추가
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addSong/{playlistId}")
    public ResponseEntity<List<SongForm>> addSongToPlaylist(@PathVariable Long playlistId, @Valid @RequestBody SongForm songForm) {
        Song song = new Song();
        song.setSongTitle(songForm.getSongTitle());
        song.setSinger(songForm.getSinger());
        song.setUrl(songForm.getUrl());

        List<Song> updatedSongList = playlistService.addSongToPlaylist(playlistId, song);
        List<SongForm> responseFormList = updatedSongList.stream()
                .map(s -> new SongForm(s.getSongTitle(), s.getSinger(), s.getUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseFormList);
    }

    // 노래 수정
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updateSong/{playlistId}/{songId}")
    public ResponseEntity<Song> updateSong(@PathVariable Long playlistId, @PathVariable Integer songId,
                                           @Valid @RequestBody SongForm songForm) {
        Song updatedSong = playlistService.updateSongFromPlaylist(playlistId, songId, songForm.getSongTitle(), songForm.getSinger(), songForm.getUrl());
        return ResponseEntity.ok(updatedSong);
    }

    // 노래 삭제
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/removeSong/{playlistId}/{songId}")
    public ResponseEntity<Playlist> removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Integer songId) {
        Playlist updatedPlaylist = playlistService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.ok(updatedPlaylist);
    }
}