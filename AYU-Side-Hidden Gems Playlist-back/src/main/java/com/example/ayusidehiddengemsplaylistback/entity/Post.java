package com.example.ayusidehiddengemsplaylistback.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id 값이 1부터 post를 작성할 때마다 자동으로 증가하게 함
    private Integer id;

    @Column(length = 200)
    private String title;

    private String content;
}
