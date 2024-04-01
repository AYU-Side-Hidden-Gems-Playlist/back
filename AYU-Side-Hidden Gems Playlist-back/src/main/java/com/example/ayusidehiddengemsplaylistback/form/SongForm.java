package com.example.ayusidehiddengemsplaylistback.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class SongForm {
    @NotEmpty(message = "노래 제목을 입력해주세요.")
    private String songTitle;

    @NotEmpty(message = "가수를 입력해주세요.")
    private String singer;

    @NotEmpty(message = "url을 입력해주세요.")
    private String url;
}
