package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.dto.PlaylistDto;
import com.example.ayusidehiddengemsplaylistback.dto.SongDto;
import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.entity.Song;
import com.example.ayusidehiddengemsplaylistback.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping("/create")
    public ResponseEntity<PlaylistDto> createPlaylist(@RequestBody Playlist playlist) {
        Playlist createdPlaylist = playlistService.createPlaylist(playlist);
        PlaylistDto playlistDto = new PlaylistDto(createdPlaylist.getPlaylistId(), createdPlaylist.getPlaylistTitle());
        return ResponseEntity.ok(playlistDto);
    }

    @GetMapping("/read/{playlistId}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable Integer playlistId) {
        Playlist playlist = playlistService.findPlaylistById(playlistId)
                .orElseThrow(() -> new RuntimeException("Post not found with playlistId " + playlistId));
        return ResponseEntity.ok(playlist);
    }

    @PutMapping("/update/{playlistId}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Integer playlistId, @RequestBody Playlist playlistDetails) {
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
    public ResponseEntity<List<SongDto>> addSongToPlaylist(@PathVariable Integer playlistId, @RequestBody SongDto songDto) {
        Song song = new Song();
        song.setSongTitle(songDto.getSongTitle());
        song.setSinger(songDto.getSinger());
        song.setUrl(songDto.getUrl());

        List<Song> updatedSongList = playlistService.addSongToPlaylist(playlistId, songDto);
        List<SongDto> responseDTOList = updatedSongList.stream()
                .map(s -> new SongDto(s.getSongId(), s.getSongTitle(), s.getSinger(), s.getUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOList);
    }

    // 노래 수정
    @PutMapping("/updateSong/{playlistId}/{songId}")
    public ResponseEntity<Song> updateSong(@PathVariable Integer playlistId, @PathVariable Integer songId,
                                           @RequestParam(value = "songTitle", required = false) String songTitle,
                                           @RequestParam(value = "singer", required = false) String singer,
                                           @RequestParam(value = "url", required = false) String url) {
        Song updatedSong = playlistService.updateSongFromPlaylist(playlistId, songId, songTitle, singer, url);
        return ResponseEntity.ok(updatedSong);
    }

    // 노래 삭제
    @DeleteMapping("/removeSong/{playlistId}/{songId}")
    public ResponseEntity<Playlist> removeSongFromPlaylist(@PathVariable Integer playlistId, @PathVariable Integer songId) {
        Playlist updatedPlaylist = playlistService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.ok(updatedPlaylist);
    }
}
