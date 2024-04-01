package com.example.ayusidehiddengemsplaylistback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer songId;

    private String songTitle;

    private String singer;

    private String url;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    @JsonBackReference
    private Playlist playlist;

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
