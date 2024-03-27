package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.entity.Post;
import com.example.ayusidehiddengemsplaylistback.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // create
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    //read
    public Optional<Post> findPostById(Integer playlistId) {
        return postRepository.findById(playlistId);
    }

    // update
    public Post updatePost(Integer playlistId, Post postDetails) {
        Post post = postRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Post not found with playlistId " + playlistId));
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        return postRepository.save(post);
    }

    // delete
    public void deletePost(Integer playlistId) {
        Post post = postRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + playlistId));
        postRepository.delete(post);
    }
}
