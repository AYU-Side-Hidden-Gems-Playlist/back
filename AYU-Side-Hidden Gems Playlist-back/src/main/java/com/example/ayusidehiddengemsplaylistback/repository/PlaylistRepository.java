package com.example.ayusidehiddengemsplaylistback.repository;

import com.example.ayusidehiddengemsplaylistback.entity.Member;
import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByMember(Member member);

}
