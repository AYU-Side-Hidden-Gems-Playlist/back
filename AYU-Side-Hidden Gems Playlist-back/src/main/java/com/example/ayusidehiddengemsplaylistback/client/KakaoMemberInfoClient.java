package com.example.ayusidehiddengemsplaylistback.client;

import com.example.ayusidehiddengemsplaylistback.dto.KakaoMemberInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://kapi.kakao.com", name = "kakaoMemberInfoClient")
public interface KakaoMemberInfoClient {

    @GetMapping(value = "/v2/user/me", consumes = "application/json")
    KakaoMemberInfoResponseDto getKakaoMemberInfo(@RequestHeader("Content-type") String contentType,
                                                  @RequestHeader("Authorization") String accessToken);

}
