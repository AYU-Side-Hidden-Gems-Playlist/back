package com.example.ayusidehiddengemsplaylistback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer songId;

    private String songTitle;

    private String singer;

    private String url;

    @ManyToOne
    @JoinColumn(name = "playlistId")
    @JsonBackReference
    private Playlist playlist;

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void updateSong(String newSongTitle, String newSinger, String newUrl) {
        if (newSongTitle != null && !newSongTitle.isEmpty()) {
            this.songTitle = newSongTitle;
        }
        if (newSinger != null && !newSinger.isEmpty()) {
            this.singer = newSinger;
        }
        if (newUrl != null && !newUrl.isEmpty()) {
            this.url = newUrl;
        }
    }
}
