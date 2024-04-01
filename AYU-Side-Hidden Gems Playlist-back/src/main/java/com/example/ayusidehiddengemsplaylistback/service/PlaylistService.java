package com.example.ayusidehiddengemsplaylistback.service;

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
                .orElseThrow(() -> new RuntimeException("Playlist not found with id " + playlistId));
        playlist.setPlaylistTitle(playlistDetails.getPlaylistTitle());
        return playlistRepository.save(playlist);
    }

    // delete
    public void deletePlaylist(Integer playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id " + playlistId));
        playlistRepository.delete(playlist);
    }

    public List<Song> addSongToPlaylist(Integer playlistId, Song song) {
        // 플레이리스트 찾기
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id " + playlistId));

        playlist.addSong(song); // Playlist 엔티티의 addSong 메소드를 사용하여 노래를 추가
        playlistRepository.save(playlist); // 변경사항을 저장한다. Cascade 설정으로 인해 Song 엔티티도 함께 저장
        return new ArrayList<>(playlist.getSongs()); // 수정된 Playlist의 Song 목록을 반환한다.
    }

    public Song updateSongFromPlaylist(Integer playlistId, Integer songId, String newSongTitle, String newSinger, String newUrl) {
        // 플레이리스트 찾기
        playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id " + playlistId));
        // 노래 찾기
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found with id: " + songId));

        // 노래 수정
        song.setSongTitle(newSongTitle);
        song.setSinger(newSinger);
        song.setUrl(newUrl);
        return songRepository.save(song); // 수정된 노래 저장
    }

    public Playlist removeSongFromPlaylist(Integer playlistId, Integer songId) {
        // 플레이리스트 찾기
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id " + playlistId));
        // 노래 찾기
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with id " + songId));

        // 노래 삭제
        playlist.removeSong(song);
        songRepository.delete(song);
        return playlist; // 수정된 Playlist 반환
    }
}
