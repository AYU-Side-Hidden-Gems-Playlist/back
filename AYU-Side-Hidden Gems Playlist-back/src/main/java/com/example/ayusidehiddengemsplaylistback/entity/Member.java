package com.example.ayusidehiddengemsplaylistback.entity;

import com.example.ayusidehiddengemsplaylistback.form.TokenForm;
import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.util.Utilities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(unique = true, length = 50, nullable = false)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 200)
    private String profile;     //프로필 사진

    @Column(name = "refresh_Token")
    private String refreshToken;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime updateTime;

    @Column
    private LocalDateTime tokenExpirationTime; //refresh token 만료시간

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<Playlist> playlists;

    @Builder
    public Member(String name, String email, String password, String profile) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile = profile;

    }

    public void updateRefreshToken(TokenForm.JwtTokenForm jwtTokenForm) {
        this.refreshToken = jwtTokenForm.getRefreshToken();
        this.tokenExpirationTime = Utilities.convertToLocalDateTime(jwtTokenForm.getRefreshTokenExpireTime());
    }

    public void expireRefreshToken(LocalDateTime now) {
        this.tokenExpirationTime = now;
    }

    public void createPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }
}