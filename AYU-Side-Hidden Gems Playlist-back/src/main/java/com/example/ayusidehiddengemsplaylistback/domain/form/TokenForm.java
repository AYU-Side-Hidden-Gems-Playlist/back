package com.example.ayusidehiddengemsplaylistback.domain.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

public class TokenForm {
    @Getter @Builder
    public static class AccessTokenResponseForm { //재발급
        private String grantType;
        private String accessToken;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpireTime;
    }

    @Builder @Getter
    @AllArgsConstructor @NoArgsConstructor
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
