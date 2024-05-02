package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.form.LikeForm;
import com.example.ayusidehiddengemsplaylistback.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/playlist")
public class LikeController {
    private final LikeService likeService;

    // 좋아요 기능
    @PostMapping("/like/{playlistId}")
    public ResponseEntity<?> like(@PathVariable Long playlistId, @RequestParam Long memberId) {
        try {
            likeService.like(new LikeForm(playlistId, memberId));
            return ResponseEntity.ok().body("Liked!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 좋아요 취소 기능
    @DeleteMapping("/like/{playlistId}")
    public ResponseEntity<?> unlike(@PathVariable Long playlistId, @RequestParam Long memberId) {
        try {
            likeService.unlike(new LikeForm(playlistId, memberId));
            return ResponseEntity.ok().body("Unliked!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}