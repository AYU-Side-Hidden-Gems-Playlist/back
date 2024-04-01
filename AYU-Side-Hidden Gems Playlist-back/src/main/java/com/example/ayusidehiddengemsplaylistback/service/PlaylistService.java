package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.dto.SongDto;
import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.ayusidehiddengemsplaylistback.entity.Song;
import com.example.ayusidehiddengemsplaylistback.repository.SongRepository;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
    }

    // create
    public Playlist createPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    //read
    public Optional<Playlist> findPlaylistById(Integer playlistId) {
        return playlistRepository.findById(playlistId);
    }

    // update
    public Playlist updatePlaylist(Integer playlistId, Playlist playlistDetails) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Post not found with playlistId " + playlistId));
        playlist.setPlaylistTitle(playlistDetails.getPlaylistTitle());
        return playlistRepository.save(playlist);
    }

    // delete
    public void deletePlaylist(Integer playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + playlistId));
        playlistRepository.delete(playlist);
    }

    public List<Song> addSongToPlaylist(Integer playlistId, SongDto songDto) {
        // 플레이리스트를 찾는다.
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id " + playlistId));

        // DTO에서 Song 엔티티로 변환한다.
        Song song = new Song();
        song.setSongTitle(songDto.getSongTitle());
        song.setSinger(songDto.getSinger());
        song.setUrl(songDto.getUrl());

        // Playlist 엔티티의 addSong 메소드를 사용하여 노래를 추가한다.
        playlist.addSong(song);

        // 변경사항을 저장한다. Cascade 설정으로 인해 Song 엔티티도 함께 저장된다.
        playlistRepository.save(playlist);

        // 새로운 노래 목록을 반환한다.
        return new ArrayList<>(playlist.getSongs()); // 수정된 Playlist의 Song 목록을 반환한다.
    }

    public Song updateSongFromPlaylist(Integer playlistId, Integer songId, String newSongTitle, String newSinger, String newUrl) {
        playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id " + playlistId));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found with id: " + songId));
        song.updateSong(newSongTitle, newSinger, newUrl); // 노래 수정
        return songRepository.save(song);
    }

    // removeSongFromPlaylist 메서드
    public Playlist removeSongFromPlaylist(Integer playlistId, Integer songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id " + playlistId));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with id " + songId));
        playlist.removeSong(song);
        songRepository.delete(song); // 노래 삭제
        return playlist; // 수정된 Playlist 반환
    }
}
