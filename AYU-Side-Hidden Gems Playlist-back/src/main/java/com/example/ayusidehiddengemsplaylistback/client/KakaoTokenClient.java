package com.example.ayusidehiddengemsplaylistback.client;

<<<<<<< HEAD
import com.example.ayusidehiddengemsplaylistback.domain.form.KakaoForm;
=======
import com.example.ayusidehiddengemsplaylistback.dto.token.KakaoTokenDto;
>>>>>>> e58dd61 (001-SignUp)
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://kauth.kakao.com", name = "kakaoTokenClient")
public interface KakaoTokenClient {

    @PostMapping(value = "/oauth/token", consumes = "application/json")
<<<<<<< HEAD
    KakaoForm.Response requestKakaoToken(@RequestHeader("Content-Type") String contentType,
                                         @SpringQueryMap KakaoForm.Request request);
=======
    KakaoTokenDto.Response requestKakaoToken(@RequestHeader("Content-Type") String contentType,
                                             @SpringQueryMap KakaoTokenDto.Request request);
>>>>>>> e58dd61 (001-SignUp)

}
