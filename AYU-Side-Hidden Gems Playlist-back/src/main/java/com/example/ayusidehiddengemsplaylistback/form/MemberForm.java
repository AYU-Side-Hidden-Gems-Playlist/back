package com.example.ayusidehiddengemsplaylistback.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
public class MemberForm {

    @NotEmpty(message = "이름을 작성해주세요.")
    @Size(max = 100)
    private String name;

    @JsonCreator
    public MemberForm(@JsonProperty("name") String name) {
        this.name = name;
    }
}
