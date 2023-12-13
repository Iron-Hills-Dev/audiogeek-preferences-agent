package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresQueryPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.UserNotFoundException;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.UserID;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;
import org.codebusters.audiogeek.preferencesagent.application.util.GenreUtils;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.repo.UserRepository;

import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public class JpaMyGenresQueryAdapter implements MyGenresQueryPort {
    private final UserRepository userRepo;
    private final GenreUtils genreUtils;

    @Override
    @Transactional
    public Set<Genre> getMyGenres(UserID id) {
        log.debug("Handling getMyGenres: id={}", id.value());
        return userRepo.findById(id.value())
                .map(u -> genreUtils.entityToGenres(u.getGenres()))
                .orElseThrow(() -> {
                    log.error("User do not exists: id={}", id.value());
                    return new UserNotFoundException();
                });
    }
}
