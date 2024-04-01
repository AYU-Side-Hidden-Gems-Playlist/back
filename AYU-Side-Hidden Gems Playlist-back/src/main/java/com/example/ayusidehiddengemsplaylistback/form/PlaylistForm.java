package com.example.ayusidehiddengemsplaylistback.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PlaylistForm {
    @NotEmpty(message = "제목을 작성해주세요.")
    @Size(max=200)
    private String playlistTitle;

}
