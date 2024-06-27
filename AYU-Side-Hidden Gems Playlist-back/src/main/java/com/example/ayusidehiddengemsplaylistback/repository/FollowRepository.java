package com.example.ayusidehiddengemsplaylistback.repository;

import com.example.ayusidehiddengemsplaylistback.entity.Follow;
import com.example.ayusidehiddengemsplaylistback.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollower(Member follower);

    List<Follow> findByFollowing(Member following);
    Optional<Follow> findByFollowerAndFollowing(Member follower, Member following);
}
