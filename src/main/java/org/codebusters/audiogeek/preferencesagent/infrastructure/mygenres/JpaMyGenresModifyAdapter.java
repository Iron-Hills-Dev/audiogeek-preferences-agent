package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresModifyPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.PutGenresCmd;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.user.UserID;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.GenreEntity;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.UserEntity;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.repo.GenreRepository;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.repo.UserRepository;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@RequiredArgsConstructor
public class JpaMyGenresModifyAdapter implements MyGenresModifyPort {
    private final UserRepository userRepo;
    private final GenreRepository genreRepo;

    @Override
    @Transactional
    public void putMyGenres(PutGenresCmd cmd) {
        log.debug("Handling PutGenresCmd: {}", cmd.toString());
        var genreEntities = cmd.genres().stream().map(this::findGenreEntity).collect(toSet());
        var user = userRepo.findById(cmd.id().value());
        user.ifPresentOrElse(u -> handleUserCreation(u, genreEntities), () -> handleUserDoNotExists(cmd.id(), genreEntities));
        log.debug("PutGenresCmd handled successfully");
    }

    //TODO: to improve performance we could find genre collection with one DB request
    // then we could persist entire collection of missing genres with one DB request
    private GenreEntity findGenreEntity(Genre genre) {
        log.trace("Searching for genre entity: {}", genre.value());
        var entity = genreRepo.findByName(genre.value());
        return entity.orElseGet(() -> createGenre(genre));
    }

    private GenreEntity createGenre(Genre genre) {
        log.trace("Genre does not exists - creating entity");
        var entity = new GenreEntity(genre.value());
        return genreRepo.save(entity);
    }

    private void handleUserDoNotExists(UserID id, Set<GenreEntity> genres) {
        log.debug("User does not exists - creating entity");
        var entity = UserEntity.builder()
                .id(id.value())
                .genres(genres)
                .build();
        userRepo.save(entity);
        log.debug("Created new user: id={}", id.value());
    }

    private void handleUserCreation(UserEntity user, Set<GenreEntity> genres) {
        log.trace("User exists - updating genres");
        user.setGenres(genres);
        userRepo.save(user);
        log.debug("User updated");
    }
}
