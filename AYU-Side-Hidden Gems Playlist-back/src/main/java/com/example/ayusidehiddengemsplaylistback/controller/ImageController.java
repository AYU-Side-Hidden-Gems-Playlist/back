package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.service.PlaylistService;
import com.example.ayusidehiddengemsplaylistback.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
public class ImageController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private PlaylistService playlistService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFileAndSavePlaylist(
            @RequestPart("file") MultipartFile file,
            @RequestPart("playlistId") Long playlistId) {
        try {
            // 이미지를 S3에 업로드하고 파일 이름(또는 URL)을 받아옴
            String fileName = s3Service.uploadFile(file);

            // 플레이리스트 정보를 조회
            Optional<Playlist> playlistOpt = playlistService.findPlaylistById(playlistId);

            Playlist playlist;
            if (playlistOpt.isPresent()) {
                // 플레이리스트가 이미 존재하는 경우, 이미지 정보 업데이트
                playlist = playlistOpt.get();
                playlist.setImageFileName(fileName);
                playlistService.updatePlaylist(playlistId, playlist);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Playlist not found with id: " + playlistId);
            }

            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }
}
