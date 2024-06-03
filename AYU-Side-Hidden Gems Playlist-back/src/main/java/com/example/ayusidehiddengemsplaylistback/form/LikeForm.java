package com.example.ayusidehiddengemsplaylistback.form;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeForm {
    private Long memberId;
    private Long playlistId;

    public LikeForm(Long memberId, Long playlistId) {
        this.memberId = memberId;
        this.playlistId = playlistId;
    }
}
