package com.example.ayusidehiddengemsplaylistback.repository;

import com.example.ayusidehiddengemsplaylistback.entity.Member;
import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import com.example.ayusidehiddengemsplaylistback.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberAndPlaylist(Member member, Playlist playlist);
}
