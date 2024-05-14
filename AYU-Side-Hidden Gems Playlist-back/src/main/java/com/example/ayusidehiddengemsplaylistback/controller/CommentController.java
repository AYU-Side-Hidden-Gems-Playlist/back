package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.form.CommentForm;
import com.example.ayusidehiddengemsplaylistback.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * playlist의 comment 조회
     */
    @GetMapping("/read/{playlistId}/comments")
    public ResponseEntity<List<CommentForm>> comments(@PathVariable Long playlistId) {
        List<CommentForm> commentList = commentService.comments(playlistId);

        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    /**
     * comment 생성
     */
    @PostMapping("/read/{playlistId}/comments")
    public ResponseEntity<CommentForm> createComment(@PathVariable Long playlistId,
                                                     @RequestBody CommentForm commentForm) {
        CommentForm createdComment = commentService.createComment(playlistId, commentForm);

        return ResponseEntity.status(HttpStatus.OK).body(createdComment);
    }

    /**
     * comment 수정
     */
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentForm> updateComment(@PathVariable Long commentId,
                                                     @RequestBody CommentForm commentForm) {
        CommentForm updatedComment = commentService.updateComment(commentId, commentForm);

        return ResponseEntity.status(HttpStatus.OK).body(updatedComment);
    }

    /**
     * comment 삭제
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommentForm> deleteComment(@PathVariable Long commentId) {
        CommentForm deletedComment = commentService.deleteComment(commentId);

        return ResponseEntity.status(HttpStatus.OK).body(deletedComment);
    }
}
