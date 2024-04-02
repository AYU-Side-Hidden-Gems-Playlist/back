package com.example.ayusidehiddengemsplaylistback.configuration;

import feign.*;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

@Slf4j
@Configuration
@EnableFeignClients(basePackages = "com.example.ayusidehiddengemsplaylistback") //todo 패키지명 수정
@Import(FeignClientsConfiguration.class)
public class FeignConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;   //BASIC
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignClientExceptionErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 2000, 3);
    }

    public static class FeignClientExceptionErrorDecoder implements ErrorDecoder {
        private ErrorDecoder errorDecoder = new Default();
        @Override
        public Exception decode(final String methodKey, Response response) {
            log.error("{} 요청 실패, status : {}, reason : {}", methodKey, response.status(), response.reason());
            FeignException exception = FeignException.errorStatus(methodKey, response);
            HttpStatus httpStatus = HttpStatus.valueOf(response.status());
            if (httpStatus.is5xxServerError()) {
                return new RetryableException(
                        response.status(),
                        exception.getMessage(),
                        response.request().httpMethod(),
                        exception,
                        null,
                        response.request());
            }
            return errorDecoder.decode(methodKey, response);
        }
    }

}
