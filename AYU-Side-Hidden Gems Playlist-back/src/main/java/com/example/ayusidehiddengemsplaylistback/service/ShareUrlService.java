package com.example.ayusidehiddengemsplaylistback.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class ShareUrlService {
    public String getPlaylistUrl(Long playlistId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()// ServletUriComponentsBuilder를 사용하여 동적으로 URL을 생성
                .path("/playlists/{playlistId}")
                .buildAndExpand(playlistId)
                .toUriString();
    }
}