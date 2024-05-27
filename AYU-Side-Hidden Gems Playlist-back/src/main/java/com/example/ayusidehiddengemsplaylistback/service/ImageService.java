package com.example.ayusidehiddengemsplaylistback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.repository.PlaylistRepository;

@Service
public class ImageService {
    private final S3Service s3Service;
    private final PlaylistRepository playlistRepository;

    @Autowired
    public ImageService(S3Service s3Service, PlaylistRepository playlistRepository) {
        this.s3Service = s3Service;
        this.playlistRepository = playlistRepository;
    }

    public boolean deleteImageAndClearUrl(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + playlistId));

        boolean isDeleted = s3Service.deleteImage(playlist.getImageUrl());
        if (isDeleted) {
            playlist.setImageUrl(null);
            playlistRepository.save(playlist);
            return true;
        } else {
            return false;
        }
    }
}
