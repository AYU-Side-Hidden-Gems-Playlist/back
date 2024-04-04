package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.entity.Song;
import com.example.ayusidehiddengemsplaylistback.form.PlaylistForm;
import com.example.ayusidehiddengemsplaylistback.form.SongForm;
import com.example.ayusidehiddengemsplaylistback.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping("/create")
    public ResponseEntity<Playlist> createPlaylist(@Valid @RequestBody PlaylistForm playlistForm) {
        Playlist playlist = new Playlist();
        playlist.setPlaylistTitle(playlistForm.getPlaylistTitle());
        Playlist createdPlaylist = playlistService.createPlaylist(playlist);
        return ResponseEntity.ok(createdPlaylist);
    }

    @GetMapping("/read/{playlistId}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable Integer playlistId) {
        Playlist playlist = playlistService.findPlaylistById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with playlistId " + playlistId));
        return ResponseEntity.ok(playlist);
    }

    @PutMapping("/update/{playlistId}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Integer playlistId, @Valid @RequestBody PlaylistForm playlistForm) {
        Playlist playlistDetails = new Playlist();
        playlistDetails.setPlaylistTitle(playlistForm.getPlaylistTitle());
        Playlist updatedPlaylist = playlistService.updatePlaylist(playlistId, playlistDetails);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @DeleteMapping("/delete/{playlistId}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Integer playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.ok().build();
    }

    // 노래 추가
    @PostMapping("/addSong/{playlistId}")
    public ResponseEntity<List<SongForm>> addSongToPlaylist(@PathVariable Integer playlistId, @Valid @RequestBody SongForm songForm) {
        Song song = new Song();
        song.setSongTitle(songForm.getSongTitle());
        song.setSinger(songForm.getSinger());
        song.setUrl(songForm.getUrl());

        List<Song> updatedSongList = playlistService.addSongToPlaylist(playlistId, song);
        List<SongForm> responseFormList = updatedSongList.stream()
                .map(s -> {
                    SongForm sf = new SongForm();
                    sf.setSongTitle(s.getSongTitle());
                    sf.setSinger(s.getSinger());
                    sf.setUrl(s.getUrl());
                    return sf;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseFormList);
    }

    // 노래 수정
    @PutMapping("/updateSong/{playlistId}/{songId}")
    public ResponseEntity<Song> updateSong(@PathVariable Integer playlistId, @PathVariable Integer songId,
                                           @Valid @RequestBody SongForm songForm) {
        Song updatedSong = playlistService.updateSongFromPlaylist(playlistId, songId, songForm.getSongTitle(), songForm.getSinger(), songForm.getUrl());
        return ResponseEntity.ok(updatedSong);
    }

    // 노래 삭제
    @DeleteMapping("/removeSong/{playlistId}/{songId}")
    public ResponseEntity<Playlist> removeSongFromPlaylist(@PathVariable Integer playlistId, @PathVariable Integer songId) {
        Playlist updatedPlaylist = playlistService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.ok(updatedPlaylist);
    }
}
