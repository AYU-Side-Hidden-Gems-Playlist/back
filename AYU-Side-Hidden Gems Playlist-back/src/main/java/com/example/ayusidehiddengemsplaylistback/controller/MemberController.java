package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{memberId}/playlists")
    public ResponseEntity<List<Playlist>> getPlaylistsByMemberId(@PathVariable Long memberId) {
        List<Playlist> playlists = memberService.findPlaylistsByMemberId(memberId);
        if(playlists.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(playlists);
    }
}