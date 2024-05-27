package com.example.ayusidehiddengemsplaylistback.controller;

import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.service.ImageService;
import com.example.ayusidehiddengemsplaylistback.service.PlaylistService;
import com.example.ayusidehiddengemsplaylistback.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final S3Service s3Service;
    private final PlaylistService playlistService;
    private final ImageService imageService;

    @Autowired
    public ImageController(S3Service s3Service, PlaylistService playlistService, ImageService imageService) {
        this.s3Service = s3Service;
        this.playlistService = playlistService;
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload/{playlistId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFileAndSavePlaylist(
            @RequestPart("file") MultipartFile file,
            @RequestPart("playlistId") Long playlistId) {
        try {
            // 이미지를 S3에 업로드하고 URL을 받아옴
            String imageUrl = s3Service.uploadFile(file);

            // 플레이리스트 정보를 조회
            Optional<Playlist> playlistOpt = playlistService.findPlaylistById(playlistId);

            if (playlistOpt.isPresent()) {
                // 플레이리스트가 이미 존재하는 경우, 이미지 URL 업데이트
                Playlist playlist = playlistOpt.get();
                playlist.setImageUrl(imageUrl);

                Playlist updatedPlaylist = playlistService.updatePlaylist(playlistId, playlist);

                return ResponseEntity.ok("File uploaded successfully: " + imageUrl);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Playlist not found with id: " + playlistId);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{playlistId}")
    public ResponseEntity<String> deleteCoverImage(@PathVariable Long playlistId) {
        boolean isDeleted = imageService.deleteImageAndClearUrl(playlistId);
        if (isDeleted) {
            return ResponseEntity.ok("Cover image deleted successfully and URL set to null.");
        } else {
            return ResponseEntity.status(500).body("Failed to delete cover image.");
        }
    }
}