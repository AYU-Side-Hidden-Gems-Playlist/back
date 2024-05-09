package com.example.ayusidehiddengemsplaylistback.form;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class SongForm {
    @NotEmpty(message = "노래 제목을 입력해주세요.")
    private String songTitle;

    @NotEmpty(message = "가수를 입력해주세요.")
    private String singer;

    @NotEmpty(message = "url을 입력해주세요.")
    private String url;

    public SongForm(String songTitle, String singer, String url) {
        this.songTitle = songTitle;
        this.singer = singer;
        this.url = url;
    }

}
