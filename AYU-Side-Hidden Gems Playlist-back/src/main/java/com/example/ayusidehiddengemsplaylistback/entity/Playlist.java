package com.example.ayusidehiddengemsplaylistback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
@Entity
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistId;

    @Column(length = 200)
    private String playlistTitle;

    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonIgnore
    private List<Song> songs;

    @ManyToOne
    @JoinColumn(name = "memberId") // member_id 컬럼을 외래 키로 사용
    @JsonBackReference
    private Member member; // 글쓴이에 대한 참조 추가


    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setMember(Optional<Member> memberOptional){
        this.member = memberOptional.orElseThrow(() -> new NoSuchElementException("Member not found"));
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

    public String getImageUrl() {
        return imageUrl;
    }


}