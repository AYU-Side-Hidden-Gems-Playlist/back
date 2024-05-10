package com.example.ayusidehiddengemsplaylistback.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
public class PlaylistForm {

    @NotEmpty(message = "제목을 작성해주세요.")
    @Size(max=200)
    private String playlistTitle;

    @JsonCreator // Jackson 라이브러리가 이 생성자를 사용하여 객체를 생성하도록 지시
    public PlaylistForm(@JsonProperty("playlistTitle") String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }
}