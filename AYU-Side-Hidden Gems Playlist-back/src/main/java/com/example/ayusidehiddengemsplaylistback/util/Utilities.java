package com.example.ayusidehiddengemsplaylistback.util;

import com.example.ayusidehiddengemsplaylistback.entity.GrantType;
import com.example.ayusidehiddengemsplaylistback.exception.BusinessException;
import com.example.ayusidehiddengemsplaylistback.exception.ErrorCode;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Utilities {

    /**
     * AuthorizationHeaderUtils
     */
    public static void validateAuthorization(String authorizationHeader) {
        // 1. authorizationHeader 필수 체크
        if (!StringUtils.hasText(authorizationHeader))
            throw new BusinessException(ErrorCode.NOT_EXISTS_AUTHORIZATION);

        // 2. authorizationHeader Bearer 체크
        String[] authorizations = authorizationHeader.split(" ");
        if (authorizations.length < 2 || !authorizations[0].equals(GrantType.BEARER.getType()))
            throw new BusinessException(ErrorCode.NOT_VALID_BEARER_GRANT_TYPE);
    }

    /**
     * DateTimeUtils
     */
    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}