package com.example.ayusidehiddengemsplaylistback.configuration;

import lombok.Getter;

@Getter
public enum GrantType {
    //베어럴 값
    BEARER("Bearer");

    GrantType(String type) {
        this.type = type;
    }

    private String type;
}
