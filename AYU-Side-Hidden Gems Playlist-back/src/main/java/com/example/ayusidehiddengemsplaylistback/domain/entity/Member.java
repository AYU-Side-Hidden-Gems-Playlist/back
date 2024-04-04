package com.example.ayusidehiddengemsplaylistback.domain.entity;
import com.example.ayusidehiddengemsplaylistback.domain.entity.base.BaseEntity;
import com.example.ayusidehiddengemsplaylistback.domain.form.TokenForm;
import com.example.ayusidehiddengemsplaylistback.util.Utilities;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MemberType memberType;

    @Column(unique = true, length = 50, nullable = false)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 200)
    private String profile;     //프로필 사진

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MemberRole role;

    @Column(name = "refresh_Token")
    private String refreshToken;

    @Column
    private LocalDateTime tokenExpirationTime; //refresh token 만료시간

    @Builder
    public Member(String name, String email, String password, String profile,
                  MemberType memberType, MemberRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.memberType = memberType;
        this.role = role;
    }

    public void updateRefreshToken(TokenForm.JwtTokenForm jwtTokenForm) {
        this.refreshToken = jwtTokenForm.getRefreshToken();
        this.tokenExpirationTime = Utilities.convertToLocalDateTime(jwtTokenForm.getRefreshTokenExpireTime());
    }

    public void expireRefreshToken(LocalDateTime now) {
        this.tokenExpirationTime = now;
    }
}