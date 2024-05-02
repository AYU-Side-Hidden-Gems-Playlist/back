package com.example.ayusidehiddengemsplaylistback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer playlistId;

    @Column(length = 200)
    private String playlistTitle;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<Song> songs;

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    // 엔티티 playlist와 song의 양방향 연결을 위한 메소드
    public void addSong(Song song) {
        songs.add(song);
        song.setPlaylist(this);
    }

    public void removeSong(Song song) {
        songs.remove(song);
        song.setPlaylist(null);
    }
}
