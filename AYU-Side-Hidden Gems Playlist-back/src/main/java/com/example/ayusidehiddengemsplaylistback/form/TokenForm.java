package com.example.ayusidehiddengemsplaylistback.domain.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

public class TokenForm {
    @Getter @Builder
    public static class AccessTokenResponseForm { //재발급
        @Schema(example = "bearer", required = true)
        private String grantType;

        @Schema(example = "Sbs7d1syzjALJujSWN30F6Sn5pG0Z45tScoKKclfAAABjqNicmqUJG13ldIf8A", required = true)
        private String accessToken;

        @Schema(example = "2024-04-03 19:07:16", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpireTime;
    }

    @Builder @Getter
    @AllArgsConstructor @NoArgsConstructor
    public static class JwtTokenForm {      //JWT
        @Schema(example = "bearer", required = true)
        private String grantType;

        @Schema(example = "Sbs7d1syzjALJujSWN30F6Sn5pG0Z45tScoKKclfAAABjqNicmqUJG13ldIf8A", required = true)
        private String accessToken;

        @Schema(example = "eMvLYqRE6UcGMh85A9iV6BUm8__nyb4A9TYKKclfAAABjqNicmWUJG13ldIf8A", required = true)
        private String refreshToken;

        @Schema(example = "2024-04-03 19:07:16", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpireTime;

        @Schema(example = "2024-04-15 19:07:16", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date refreshTokenExpireTime;
    }
}
