package com.example.ayusidehiddengemsplaylistback.domain.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

public class KakaoForm {

    /**
     * kakao 스펙에 맞춘 설정
     * Kakao API 요청/응답/정보
     * */

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

    @Getter
    public static class MemberInfoResponse {
        private String id;

        @JsonProperty("kakao_account")
        private KakaoAccount kakaoAccount;

        @Getter
        public static class KakaoAccount {
            private String email;
            private Profile profile;

            @Getter
            public static class Profile {
                private String nickname;

                @JsonProperty("thumbnail_image_url")
                private String thumbnailImageUrl;
            }
        }

    }
}
