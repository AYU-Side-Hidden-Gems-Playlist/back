package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.entity.Post;
import com.example.ayusidehiddengemsplaylistback.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }


    @GetMapping("/read/{playlistId}")
    public ResponseEntity<Post> getPostById(@PathVariable Integer playlistId) {
        Post post = postService.findPostById(playlistId)
                .orElseThrow(() -> new RuntimeException("Post not found with playlistId " + playlistId));
        return ResponseEntity.ok(post);
    }

    @PutMapping("/update/{playlistId}")
    public ResponseEntity<Post> updatePost(@PathVariable Integer playlistId, @RequestBody Post postDetails) {
        Post updatedPost = postService.updatePost(playlistId, postDetails);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/delete/{playlistId}")
    public ResponseEntity<?> deletePost(@PathVariable Integer playlistId) {
        postService.deletePost(playlistId);
        return ResponseEntity.ok().build();
    }
}
