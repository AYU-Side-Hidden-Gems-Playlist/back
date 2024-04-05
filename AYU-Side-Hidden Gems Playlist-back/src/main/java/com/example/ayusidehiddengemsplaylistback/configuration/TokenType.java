package com.example.ayusidehiddengemsplaylistback.configuration;

public enum TokenType {
    ACCESS, REFRESH;

    public static boolean isAccessToken(String tokenType) {
        return tokenType.equals(TokenType.ACCESS.name());
    }
}