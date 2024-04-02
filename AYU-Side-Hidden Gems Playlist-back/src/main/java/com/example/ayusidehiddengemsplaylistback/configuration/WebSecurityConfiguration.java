package com.example.ayusidehiddengemsplaylistback.configuration;

import com.example.ayusidehiddengemsplaylistback.service.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class WebSecurityConfiguration implements WebMvcConfigurer {

    @Value("${token.access-token-expiration-time}")
    private String accessTokenExpirationTime;

    @Value("${token.refresh-token-expiration-time}")
    private String refreshTokenExpirationTime;

    @Value("${token.secret}")
    private String secretToken;


    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }

    public class AuditorAwareImpl implements AuditorAware<String> {
        @Autowired
        HttpServletRequest httpServletRequest;

        @Override
        public Optional<String> getCurrentAuditor() {
            String modifiedBy = httpServletRequest.getRequestURI();
            if(!StringUtils.hasText(modifiedBy))
                modifiedBy = "unknown";

            return Optional.of(modifiedBy);
        }
    }


    @Bean
    public TokenManager tokenManager() {
        return new TokenManager(accessTokenExpirationTime, refreshTokenExpirationTime, secretToken);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") //어떤 url로 오면 허용할건지 정하기
                .allowedOrigins("*") //모든 origin 허용
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name()
                );
    }
}
