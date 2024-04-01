package com.example.ayusidehiddengemsplaylistback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {
    // 노래를 addSong 메소드로 추가할 때 playlist 엔티티는 건들이지 않고 song list만 수정(노래 추가) 하는 dto

    private Integer songId;
    private String songTitle;
    private String singer;
    private String url;
}