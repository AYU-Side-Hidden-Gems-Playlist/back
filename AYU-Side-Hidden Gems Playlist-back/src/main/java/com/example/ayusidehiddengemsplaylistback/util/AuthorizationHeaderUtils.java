package com.example.ayusidehiddengemsplaylistback.util;

import com.example.ayusidehiddengemsplaylistback.configuration.GrantType;
import com.example.ayusidehiddengemsplaylistback.error.ErrorCode;
import com.example.ayusidehiddengemsplaylistback.error.exception.AuthenticationException;
import org.springframework.util.StringUtils;

public class AuthorizationHeaderUtils {
    public static void validateAuthorization(String authorizationHeader) {
        // 1. authorizationHeader 필수 체크
        if (!StringUtils.hasText(authorizationHeader))
            throw new AuthenticationException(ErrorCode.NOT_EXISTS_AUTHORIZATION);

        // 2. authorizationHeader Bearer 체크
        String[] authorizations = authorizationHeader.split(" ");
        if (authorizations.length < 2 || !authorizations[0].equals(GrantType.BEARER.getType()))
            throw new AuthenticationException(ErrorCode.NOT_VALID_BEARER_GRANT_TYPE);
    }

}