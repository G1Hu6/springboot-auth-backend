package com.security.services;

import com.security.entities.SessionEntity;
import com.security.entities.UserEntity;
import com.security.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private static final Integer SESSION_LIMIT = 2;

    public void generateNewSession(UserEntity user, String refreshToken){

        List<SessionEntity> loggedSessions = sessionRepository.findByUser(user);
        if(loggedSessions.size() == SESSION_LIMIT){
            // Sort all logged session according to last used first
            loggedSessions.sort(Comparator.comparing(SessionEntity::getLastUsedAt));
            log.info("Logged Sessions : {}",loggedSessions);

            SessionEntity lastRecentlyUsedSession = loggedSessions.getFirst();
            sessionRepository.delete(lastRecentlyUsedSession);
        }

        SessionEntity newSession = SessionEntity.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();

        // Save newly created session in database...
        sessionRepository.save(newSession);
    }

    public void validateSession(String refreshToken){
        // Check that a valid session is present for logged-in user with refresh token
        SessionEntity session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new SessionAuthenticationException("No active session found for refresh token"));

        // Update the last recently used time and update changes in database
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
}
