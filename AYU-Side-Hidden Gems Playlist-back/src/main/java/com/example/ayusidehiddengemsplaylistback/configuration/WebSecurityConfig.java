package com.example.ayusidehiddengemsplaylistback.configuration;

import com.example.ayusidehiddengemsplaylistback.filter.JwtAuthenticationFilter;
import com.example.ayusidehiddengemsplaylistback.filter.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenManager tokenManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // todo: 엔드포인트 액세스 규칙
        http
                .httpBasic().disable()
                .csrf().disable()        //csrf 비활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("logout"))
                .logoutSuccessUrl("/") //로그아웃 성공시 이동할 url
                .invalidateHttpSession(true) //로그아웃시 생성된 세션 삭제 활성화
                .and()

                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(tokenManager), UsernamePasswordAuthenticationFilter.class);
//                     Put JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter(검증->추출)

        return http.build();
    }


}
