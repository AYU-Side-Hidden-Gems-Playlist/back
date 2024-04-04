package com.example.ayusidehiddengemsplaylistback.domain.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

public class LoginForm {        //요청을 받을 객체, 요청을 반환할 객체

    @Getter
    public static class Request {
        @Schema(description = "소셜 로그인 회원 타입", example = "kakao", required = true)
        private String memberType;
    }

    @Getter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        /** 반환 정보: 액세스 토큰, 리프레쉬 토큰 */
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
