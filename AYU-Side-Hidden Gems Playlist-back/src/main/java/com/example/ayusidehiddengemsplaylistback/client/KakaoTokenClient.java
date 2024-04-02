package com.example.ayusidehiddengemsplaylistback.client;

import com.example.ayusidehiddengemsplaylistback.domain.form.KakaoForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://kauth.kakao.com", name = "kakaoTokenClient")
public interface KakaoTokenClient {

    @PostMapping(value = "/oauth/token", consumes = "application/json")
    KakaoForm.Response requestKakaoToken(@RequestHeader("Content-Type") String contentType,
                                         @SpringQueryMap KakaoForm.Request request);

}
