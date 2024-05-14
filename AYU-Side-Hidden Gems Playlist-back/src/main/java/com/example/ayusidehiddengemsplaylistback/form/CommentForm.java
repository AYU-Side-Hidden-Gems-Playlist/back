package com.example.ayusidehiddengemsplaylistback.form;

import com.example.ayusidehiddengemsplaylistback.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentForm {
    private String body;

    public static CommentForm toCommentForm(Comment comment) {
        return CommentForm.builder()
                .body(comment.getBody())
                .build();
    }
}
