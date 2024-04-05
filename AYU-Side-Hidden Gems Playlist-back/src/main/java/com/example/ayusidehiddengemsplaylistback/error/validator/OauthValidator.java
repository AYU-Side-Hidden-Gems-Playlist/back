package com.example.ayusidehiddengemsplaylistback.error.validator;

import com.example.ayusidehiddengemsplaylistback.domain.MemberType;
import com.example.ayusidehiddengemsplaylistback.error.ErrorCode;
import com.example.ayusidehiddengemsplaylistback.error.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class OauthValidator {
    public void validateMemberType(String memberType) {
        if(!MemberType.isMemberType(memberType))
            throw new AuthenticationException(ErrorCode.INVALID_MEMBER_TYPE);
    }
}