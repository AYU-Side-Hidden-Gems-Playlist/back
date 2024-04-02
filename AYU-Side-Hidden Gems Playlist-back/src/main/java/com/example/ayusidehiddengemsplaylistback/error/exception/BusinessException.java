package com.example.ayusidehiddengemsplaylistback.error.exception;

import com.example.ayusidehiddengemsplaylistback.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
