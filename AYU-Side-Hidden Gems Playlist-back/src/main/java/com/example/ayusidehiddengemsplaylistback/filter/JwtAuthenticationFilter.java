package com.example.ayusidehiddengemsplaylistback.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenManager tokenManager;

    /** 서블릿 필터
     * 1. 들어오는 request를 가로채어 Authorization header에서 JWT 토큰을 추출
     * 2. TokenManager를 통한 토큰의 유효성 검사
     * 3. 유효할 경우 TokenManager를 사용해서 인증 정보 검색
     * 4. 보안 컨텍스트에 인증 정보를 설정을 수행합니다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         // JWT 추출
         String token = extractTokenFromRequest(request);
        log.info("doFilterInternal token: {}", token);
        // 유효성 검사
        if (StringUtils.hasText(token)) {
            tokenManager.validateToken(token);
            Authentication authentication = tokenManager.getAuthentication(token);
            if (authentication != null) {
                // 통과 -> 보안 컨텍스트에 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization"); // Assuming the access token is provided in the Authorization header
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7); // Remove the "Bearer " prefix
        }
        return null;
    }
}