package com.example.ayusidehiddengemsplaylistback.service.social;

import com.example.ayusidehiddengemsplaylistback.dto.OAuthAttributes;

public interface SocialLoginApiService {
    OAuthAttributes getMemberInfo(String accessToken);
}
