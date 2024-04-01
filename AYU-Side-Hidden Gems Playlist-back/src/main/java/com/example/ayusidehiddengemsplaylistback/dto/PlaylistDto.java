package com.example.ayusidehiddengemsplaylistback.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDto {
    // playlist를 create 메소드로 생성할 때 song list는 생성하지 않고 id랑 title만 생성하도록 하는 dto

    private Integer playlistId;
    private String playlistTitle;
}
