package com.example.ayusidehiddengemsplaylistback.dto.token;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Builder @Getter
@AllArgsConstructor @NoArgsConstructor
public class JwtTokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date accessTokenExpireTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date refreshTokenExpireTime;
}
