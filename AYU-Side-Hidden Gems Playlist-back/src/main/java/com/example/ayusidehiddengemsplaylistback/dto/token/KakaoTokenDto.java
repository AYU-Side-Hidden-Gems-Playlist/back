package com.example.ayusidehiddengemsplaylistback.dto.token;

import lombok.*;

public class KakaoTokenDto {

    /** kakao 스펙에 맞춘 설정 */

    @Builder @Getter
    public static class Request {
        private String grant_type;
        private String client_id;
        private String redirect_uri;
        private String code;
        private String client_secret;
    }

    @ToString
    @Builder @Getter
    @AllArgsConstructor @NoArgsConstructor
    public static class Response {
        private String access_token;
        private String token_type;
        private String refresh_token;
        private String expires_in;
        private String scope;
        private String refresh_token_expires_in;
    }
}
