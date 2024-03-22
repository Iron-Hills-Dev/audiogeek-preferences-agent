package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresQueryPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.user.UserID;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.GenreEntity;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.repo.UserRepository;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Slf4j
public class JpaMyGenresQueryAdapter implements MyGenresQueryPort {
    private final UserRepository userRepo;
    private final GenreFactory genreFactory;

    @Override
    @Transactional
    public Set<Genre> getMyGenres(UserID id) {
        log.debug("Handling getMyGenres: id={}", id.value());
        return userRepo.findById(id.value())
                .map(u -> entityToGenres(u.getGenres()))
                .orElse(Set.of());
    }

    private Set<Genre> entityToGenres(Set<GenreEntity> genres) {
        return genres.stream()
                .map(g -> genreFactory.createGenre(g.getName()))
                .collect(toSet());
    }
}
