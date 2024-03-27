package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.service.ShareUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ShareUrlController {
    private final ShareUrlService shareUrlService;

    @GetMapping("/playlists/{playlistId}") // playlistId는 경로변수
    public ResponseEntity<Map<String, String>> getPlaylistUrl(@PathVariable Long playlistId) {
        String url = shareUrlService.getPlaylistUrl(playlistId); // url을 실제 URL키로 지정
        Map<String, String> response = Collections.singletonMap("url", url);
        return ResponseEntity.ok().body(response);
    }
}
