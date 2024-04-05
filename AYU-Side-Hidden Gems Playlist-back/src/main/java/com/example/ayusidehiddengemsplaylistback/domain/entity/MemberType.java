package com.example.ayusidehiddengemsplaylistback.domain.entity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MemberType {
    kakao

    ;
    public static MemberType from(String memberType) {
        return MemberType.valueOf(memberType.toLowerCase());
    }

    public static boolean isMemberType(String memberType) {
        List<MemberType> memberTypes = Arrays.stream(MemberType.values())
                .filter(type -> type.name().equals(memberType))
                .collect(Collectors.toList());
        return !memberTypes.isEmpty();
    }
}
