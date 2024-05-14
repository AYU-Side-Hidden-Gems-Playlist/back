package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.entity.Comment;
import com.example.ayusidehiddengemsplaylistback.entity.Member;
import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.exception.BusinessException;
import com.example.ayusidehiddengemsplaylistback.exception.ErrorCode;
import com.example.ayusidehiddengemsplaylistback.form.CommentForm;
import com.example.ayusidehiddengemsplaylistback.repository.CommentRepository;
import com.example.ayusidehiddengemsplaylistback.repository.MemberRepository;
import com.example.ayusidehiddengemsplaylistback.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PlaylistRepository playlistRepository;
    private final MemberRepository memberRepository;


    /** select */
    @Transactional(readOnly = true)
    public List<CommentForm> comments(Long playlistId) {
        List<Comment> comments = commentRepository.findByPlaylistId(playlistId);
        List<CommentForm> commentForms = new ArrayList<>();

        for (Comment comment : comments) {
            CommentForm commentForm = CommentForm.toCommentForm(comment);
            commentForms.add(commentForm);
        }

        return commentForms;
    }

    /** create */
    public CommentForm createComment(Long playlistId, CommentForm commentForm) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLAYLIST_NOT_FOUND));
        Member member = memberRepository.findByMemberId(playlist.getMember().getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_WRITER_NOT_FOUND));

        Comment comment = Comment.toCommentEntity(commentForm, playlist, member);
        Comment saved = commentRepository.save(comment);

        return CommentForm.toCommentForm(saved);
    }

    /** update */
    public CommentForm updateComment(Long commentId, CommentForm commentForm) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        comment.patch(commentForm);
        Comment updated = commentRepository.save(comment);

        return CommentForm.toCommentForm(updated);

    }

    /** delete */
    public CommentForm deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        commentRepository.delete(comment);

        return CommentForm.toCommentForm(comment);
    }
}
