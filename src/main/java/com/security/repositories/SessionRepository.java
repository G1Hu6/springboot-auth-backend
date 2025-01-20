package com.security.repositories;

import com.security.entities.SessionEntity;
import com.security.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    List<SessionEntity> findByUser(UserEntity user);

    Optional<SessionEntity> findByRefreshToken(String refreshToken);
}
