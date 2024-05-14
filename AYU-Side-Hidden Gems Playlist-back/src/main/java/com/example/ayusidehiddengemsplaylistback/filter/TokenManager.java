package com.example.ayusidehiddengemsplaylistback.filter;

import com.example.ayusidehiddengemsplaylistback.entity.GrantType;
import com.example.ayusidehiddengemsplaylistback.entity.TokenType;
import com.example.ayusidehiddengemsplaylistback.exception.BusinessException;
import com.example.ayusidehiddengemsplaylistback.form.TokenForm;
import com.example.ayusidehiddengemsplaylistback.exception.ErrorCode;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenManager {
    /**
     * 토큰을 관리합니다.
     */
    @Value("${token.access-token-expiration-time}")
    private String accessTokenExpirationTime;

    @Value("${token.refresh-token-expiration-time}")
    private String refreshTokenExpirationTime;

    @Value("${token.secret}")
    private String tokenSecret;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        tokenSecret = Base64.getEncoder().encodeToString(tokenSecret.getBytes());
    }

    public TokenForm.JwtTokenForm generateJwtTokenFormByEmail(String email, Set<String> roles) {
        Date accessTokenExpireTime = returnAccessTokenExpireTime();
        Date refreshTokenExpireTime = returnRefreshTokenExpireTime();

        String accessToken = generateAccessTokenByEmail(email, roles, accessTokenExpireTime);
        String refreshToken = generateRefreshTokenByEmail(email, roles, refreshTokenExpireTime);

        return TokenForm.JwtTokenForm.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }


    /** 토큰 생성 메서드 By Email */
    public String generateAccessTokenByEmail(String email, Set<String> roles, Date expirationTime) {
        return Jwts.builder()
                .setSubject(TokenType.ACCESS.name())        //token title
                .setIssuedAt(new Date())                //발급시간: 현재
                .setExpiration(expirationTime)      //만료시간
                .claim("email", email)    //회원 아이디
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes(StandardCharsets.UTF_8))
                .setHeaderParam("type", "JWT")
                .compact();
    }

    public String generateRefreshTokenByEmail(String email, Set<String> roles, Date expirationTime) {
        return Jwts.builder()
                .setSubject(TokenType.REFRESH.name())   //token title
                .setIssuedAt(new Date())                //발급시간: 현재
                .setExpiration(expirationTime)          //만료시간
                .claim("email", email)              //회원 아이디
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes(StandardCharsets.UTF_8))
                .setHeaderParam("type", "JWT")
                .compact();
    }


    /** 만료 시간 반환 메서드 */
    public Date returnAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    public Date returnRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }


    /**
     * token 검증
     */
    public void validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            log.info("만료된 token 입니다.", e);
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            log.info("유효하지 않은 token 입니다.", e);
            throw new BusinessException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

    /**
     * 토큰 클레임 반환
     */
    public Claims getTokenClaims(String token) {
        Claims claims;
        try {
            // JWT 토큰을 구문 분석하여 서명 유효 여부를 판별 후, 반환된 객체
            claims = Jwts.parser().setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();
        } catch(Exception e) {
            log.info("유효하지 않은 token 입니다.", e);
            throw new AuthenticationCredentialsNotFoundException(token);
        }

        return claims;
    }

    /**
     * 토큰 인증 객체 반환
     * 사용자의 인증 정보를 추출하여 이를 기반으로 사용자 인증 작업 수행
     * UserDetailsService를 통해 인증 객체 생성
     */
    public Authentication getAuthentication(String token) {
        String email = getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
        return claims.get("email", String.class);
    }

}