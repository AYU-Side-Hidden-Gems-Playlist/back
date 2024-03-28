package com.example.ayusidehiddengemsplaylistback.repository;

<<<<<<< HEAD
import com.example.ayusidehiddengemsplaylistback.domain.entity.Member;
=======
import com.example.ayusidehiddengemsplaylistback.domain.Member;
>>>>>>> e58dd61 (001-SignUp)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByRefreshToken(String refreshToken);
}