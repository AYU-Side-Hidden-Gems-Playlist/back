package com.example.ayusidehiddengemsplaylistback.error.exception;

import com.example.ayusidehiddengemsplaylistback.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}