package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.entity.GrantType;
import com.example.ayusidehiddengemsplaylistback.entity.TokenType;
import com.example.ayusidehiddengemsplaylistback.form.TokenForm;
import com.example.ayusidehiddengemsplaylistback.error.ErrorCode;
import com.example.ayusidehiddengemsplaylistback.error.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class TokenManager {

    private final String accessTokenExpirationTime;
    private final String refreshTokenExpirationTime;
    private final String tokenSecret;

    public TokenForm.JwtTokenForm generateJwtTokenForm(Long memberId) {
        Date accessTokenExpireTime = returnAccessTokenExpireTime();
        Date refreshTokenExpireTime = returnRefreshTokenExpireTime();

        String accessToken = generateAccessToken(memberId, accessTokenExpireTime);
        String refreshToken = generateRefreshToken(memberId, refreshTokenExpireTime);

        return TokenForm.JwtTokenForm.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    /** 토큰 생성 메서드 */
    public String generateAccessToken(Long memberId, Date expirationTime) {
        String accessToken = Jwts.builder()
                .setSubject(TokenType.ACCESS.name())        //token title
                .setIssuedAt(new Date())                //발급시간: 현재
                .setExpiration(expirationTime)      //만료시간
                .claim("memberId", memberId)    //회원 아이디
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes(StandardCharsets.UTF_8))
                .setHeaderParam("type", "JWT")
                .compact();

        return accessToken;
    }

    public String generateRefreshToken(Long memberId, Date expirationTime) {
        String refreshToken = Jwts.builder()
                .setSubject(TokenType.REFRESH.name())        //token title
                .setIssuedAt(new Date())                //발급시간: 현재
                .setExpiration(expirationTime)      //만료시간
                .claim("memberId", memberId)    //회원 아이디
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes(StandardCharsets.UTF_8))
                .setHeaderParam("type", "JWT")
                .compact();

        return refreshToken;
    }

    /** 만료 시간 반환 메서드 */
    public Date returnAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    public Date returnRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }

    /** token 검증 */
    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            log.info("만료된 token 입니다.", e);
            throw new AuthenticationException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            log.info("유효하지 않은 token 입니다.", e);
            throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

    public Claims getTokenClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();
        } catch(Exception e) {
            log.info("유효하지 않은 token 입니다.", e);
            throw new AuthenticationCredentialsNotFoundException(token);
        }

        return claims;
    }

}
