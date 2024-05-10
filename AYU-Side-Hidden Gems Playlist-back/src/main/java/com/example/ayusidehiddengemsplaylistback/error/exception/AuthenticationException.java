package com.example.ayusidehiddengemsplaylistback.error.exception;

import com.example.ayusidehiddengemsplaylistback.error.ErrorCode;

public class AuthenticationException extends BusinessException {
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}