package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.PutGenresCmd;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.user.UserID;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.GenreEntity;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.UserEntity;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.repo.GenreRepository;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class JpaMyGenresModifyAdapterTest {
    @Autowired
    private GenreFactory genreFactory;
    @Autowired
    private GenreRepository genreRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JpaMyGenresModifyAdapter sut;

    @BeforeEach
    void clearDatabase() {
        userRepo.deleteAll();
        genreRepo.deleteAll();
    }

    @Test
    @DisplayName("Test if putMyGenres works correctly for user and genres not in DB")
    void putGenresUserNotInDbGenreNotInDb() {
        // given
        var cmd = PutGenresCmd.builder()
                .id(new UserID(randomUUID()))
                .genres(Set.of(genreFactory.createGenre("rock"), genreFactory.createGenre("pop")))
                .build();

        // when
        sut.putMyGenres(cmd);

        // then
        var exceptedEntity = UserEntity.builder()
                .id(cmd.id().value())
                .genres(genresToEntity(cmd.genres()))
                .build();

        assertThat(userRepo.findById(cmd.id().value()))
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(exceptedEntity);
    }

    @Test
    @DisplayName("Test if putMyGenres works correctly for user with no genres in DB and genres not in DB")
    void putGenresUserWithNoGenresGenreNotInDb() {
        // given
        var cmd = PutGenresCmd.builder()
                .id(new UserID(randomUUID()))
                .genres(Set.of(genreFactory.createGenre("rock"), genreFactory.createGenre("pop")))
                .build();
        userRepo.save(UserEntity.builder().id(cmd.id().value()).genres(Set.of()).build());

        // when
        sut.putMyGenres(cmd);

        // then
        var exceptedEntity = UserEntity.builder()
                .id(cmd.id().value())
                .genres(genresToEntity(cmd.genres()))
                .build();

        assertThat(userRepo.findById(cmd.id().value()))
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(exceptedEntity);
    }

    @Test
    @DisplayName("Test if putMyGenres works correctly for user not in DB and genre in DB")
    void putGenresUserNotInDbGenreInDb() {
        // given
        var cmd = PutGenresCmd.builder()
                .id(new UserID(randomUUID()))
                .genres(Set.of(genreFactory.createGenre("rock")))
                .build();
        genreRepo.save(new GenreEntity("rock"));

        // when
        sut.putMyGenres(cmd);

        // then
        var exceptedEntity = UserEntity.builder()
                .id(cmd.id().value())
                .genres(genresToEntity(cmd.genres()))
                .build();

        assertThat(userRepo.findById(cmd.id().value()))
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(exceptedEntity);
    }

    @Test
    @DisplayName("Test if putMyGenres works correctly for user in DB with genre, genre added not in DB")
    void putGenresUserWithGenre() {
        // given
        var cmd = PutGenresCmd.builder()
                .id(new UserID(randomUUID()))
                .genres(Set.of(genreFactory.createGenre("indie"), genreFactory.createGenre("pop")))
                .build();
        genreRepo.save(new GenreEntity("rock"));
        userRepo.save(UserEntity.builder()
                .id(cmd.id().value())
                .genres(Set.of(genreRepo.findByName("rock").get()))
                .build());

        // when
        sut.putMyGenres(cmd);

        // then
        var exceptedEntity = UserEntity.builder()
                .id(cmd.id().value())
                .genres(genresToEntity(cmd.genres()))
                .build();

        assertThat(userRepo.findById(cmd.id().value()))
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(exceptedEntity);
    }

    @Test
    @DisplayName("Test what happens with genre table when genre is removed from user table")
    void putGenresDeleteGenre() {
        // given
        var cmd = PutGenresCmd.builder()
                .id(new UserID(randomUUID()))
                .genres(Set.of(genreFactory.createGenre("metal")))
                .build();
        genreRepo.save(new GenreEntity("rock"));
        userRepo.save(UserEntity.builder()
                .id(cmd.id().value())
                .genres(Set.of(genreRepo.findByName("rock").get()))
                .build());

        // when
        sut.putMyGenres(cmd);

        // then
        genreRepo.findByName("rock").ifPresentOrElse(
                g -> log.warn("Genre still exists after removal"),
                () -> log.warn("Genre do not exists after removal")
        );
    }

    @Test
    @DisplayName("Test what happens when multiple threads try to put genres to one user")
    public void putGenresMultiThreaded() {
        // given
        var id = new UserID(UUID.randomUUID());

        // when
        try {
            var futures = IntStream.rangeClosed(1, 5)
                    .mapToObj(i -> runAsync(genreateUniqueRunnable(id, Integer.toString(i))))
                    .toArray(CompletableFuture<?>[]::new);
            CompletableFuture.allOf(futures).get();
        } catch (Exception ignored) {
        }

        // then
        var user = userRepo.findById(id.value());
        log.info("--------TEST RESULT--------");
        user.ifPresentOrElse(u -> log.info("Created user with genres: {}", u.getGenres()
                .stream()
                .map(GenreEntity::getName)
                .toList()), () -> log.info("User was not created"));
        log.info("--------TEST RESULT--------");
    }

    private Runnable genreateUniqueRunnable(UserID id, String runnableId) {
        var genre = Set.of(genreFactory.createGenre(runnableId));
        log.info("TEST: Created runnable with id: {}", runnableId);
        return () -> sut.putMyGenres(new PutGenresCmd(id, genre));
    }

    private Set<GenreEntity> genresToEntity(Set<Genre> genres) {
        return genres.stream()
                .map(g -> new GenreEntity(g.value()))
                .collect(toSet());
    }
}
