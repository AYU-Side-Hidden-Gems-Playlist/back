package com.example.ayusidehiddengemsplaylistback.entity;

import com.example.ayusidehiddengemsplaylistback.form.CommentForm;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "playlistId")
    private Playlist playlist;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member; //이름

    @Column(length = 200)
    private String body;


    public static Comment toCommentEntity(CommentForm commentForm, Playlist playlist, Member member) {
        return new Comment(
                null,
                playlist,
                member,
                commentForm.getBody()
        );
    }

    public void patch(CommentForm commentForm) {
        if(commentForm.getBody() != null)
            this.body = commentForm.getBody();
    }
}
