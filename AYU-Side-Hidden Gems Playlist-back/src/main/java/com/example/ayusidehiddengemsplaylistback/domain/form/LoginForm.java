package com.example.ayusidehiddengemsplaylistback.domain.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

public class LoginForm {        //요청을 받을 객체, 요청을 반환할 객체

    @Getter
    public static class Request {
        private String memberType;
    }

    @Getter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        /** 반환 정보: 액세스 토큰, 리프레쉬 토큰 */
        private String grantType;
        private String accessToken;
        private String refreshToken;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpireTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date refreshTokenExpireTime;

        public static Response of(TokenForm.JwtTokenForm jwtTokenDto) {
            return Response.builder()
                    .grantType(jwtTokenDto.getGrantType())
                    .accessToken(jwtTokenDto.getAccessToken())
                    .accessTokenExpireTime(jwtTokenDto.getAccessTokenExpireTime())
                    .refreshToken(jwtTokenDto.getRefreshToken())
                    .refreshTokenExpireTime(jwtTokenDto.getRefreshTokenExpireTime())
                    .build();
        }
    }
}
