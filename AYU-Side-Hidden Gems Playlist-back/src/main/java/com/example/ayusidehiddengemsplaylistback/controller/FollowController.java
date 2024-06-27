package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.form.FollowForm;
import com.example.ayusidehiddengemsplaylistback.form.MemberForm;
import com.example.ayusidehiddengemsplaylistback.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@RequestParam Long followerId, @RequestParam Long followingId) {
        followService.follow(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Void> unfollow(@RequestParam Long followerId, @RequestParam Long followingId) {
        followService.unfollow(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{memberId}/followers")
    public ResponseEntity<List<MemberForm>> getFollowers(@PathVariable Long memberId) {
        List<MemberForm> followers = followService.getFollowers(memberId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{memberId}/following")
    public ResponseEntity<List<MemberForm>> getFollowings(@PathVariable Long memberId) {
        List<MemberForm> following = followService.getFollowings(memberId);
        return ResponseEntity.ok(following);
    }
}
