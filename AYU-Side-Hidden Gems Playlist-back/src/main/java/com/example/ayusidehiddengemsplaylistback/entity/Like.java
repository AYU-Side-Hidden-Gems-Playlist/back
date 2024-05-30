package com.example.ayusidehiddengemsplaylistback.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeId")
    private Long likeid;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "playlistId")
    private Playlist playlist;

    @Builder
    public Like(Member member, Playlist playlist) {
        this.member = member;
        this.playlist = playlist;
    }
}
