package com.example.ayusidehiddengemsplaylistback.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "followerId")
    private Member follower;

    @ManyToOne
    @JoinColumn(name = "followingId")
    private Member following;
}
