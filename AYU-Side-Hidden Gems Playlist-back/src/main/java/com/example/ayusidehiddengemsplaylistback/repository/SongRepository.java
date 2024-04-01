package com.example.ayusidehiddengemsplaylistback.repository;

import com.example.ayusidehiddengemsplaylistback.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SongRepository extends JpaRepository<Song, Integer> {
}