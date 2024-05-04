package com.example.ayusidehiddengemsplaylistback.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 인증
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-001", "해당 token은 만료된 token입니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-002", "해당 token은 유효한 token이 아닙니다."),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A-003", "해당 Authorization Header가 비었습니다"),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A-004", "해당 Authorization Header의 인증 타입이 BEARER가 아닙니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A-005", "해당 refresh token이 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-006", "해당 refresh token은 만료된 token입니다."),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "A-007", "해당 token은 access token이 아닙니다."),
    NOT_VALID_JWT_SIGN(HttpStatus.UNAUTHORIZED, "A-008", "JWT signature이 일치하지 않습니다."),

    // member
    INVALID_MEMBER_TYPE(HttpStatus.BAD_REQUEST, "M-001", "잘못된 회원 타입입니다. (memberType: kakao)"),
    ALREADY_REGISTERED_MEMBER(HttpStatus.BAD_REQUEST, "M-002", "이미 가입 된 회원입니다."),
    MEMBER_NOT_EXISTS(HttpStatus.NOT_FOUND, "M-003", "해당 회원을 찾을 수 없습니다."),

    // playlist
    PLAYLIST_NOT_FOUND(HttpStatus.NOT_FOUND, "P-001", "해당 플레이리스트를 찾을 수 없습니다."),

    // comment
    COMMENT_WRITER_NOT_FOUND(HttpStatus.NOT_FOUND, "C-001", "댓글 작성자를 찾을 수 없습니다. 만일 회원 가입이 되어있지 않다면 진행해주세요!"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C-002", "해당 댓글을 찾을 수 없습니다."),

    ;

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;
}
