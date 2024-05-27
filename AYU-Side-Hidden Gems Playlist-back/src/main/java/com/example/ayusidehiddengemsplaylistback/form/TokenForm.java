package com.example.ayusidehiddengemsplaylistback.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

public class TokenForm {
    @Getter
    @Builder
    public static class AccessTokenResponseForm { //재발급
        @Schema(example = "bearer", required = true)
        private String grantType;

        @Schema(example = "Sbs7d1syzjALJujSWN30F6Sn5pG0Z45tScoKKclfAAABjqNicmqUJG13ldIf8A", required = true)
        private String accessToken;

        @Schema(example = "2024-04-03 19:07:16", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpireTime;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JwtTokenForm {      //JWT
        private String grantType;
        private String accessToken;
        private String refreshToken;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpireTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date refreshTokenExpireTime;
    }
}
