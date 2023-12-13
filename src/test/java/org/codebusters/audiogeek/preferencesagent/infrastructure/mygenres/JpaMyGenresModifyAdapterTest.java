package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.PutGenresCmd;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.UserID;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
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

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class JpaMyGenresModifyAdapterTest {
    @Autowired
    private GenreFactory genreFactory;
    @Autowired
    private GenreRepository genreRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JpaMyGenresModifyAdapter adapter;

    @BeforeEach
    void clearDatabase() {
        userRepo.deleteAll();
        genreRepo.deleteAll();
    }

    @Test
    @DisplayName("Test if putMyGenres works correctly for not existing user")
    void putGenresUserDoNotExists() {
        // given
        var cmd = PutGenresCmd.builder()
                .id(new UserID(randomUUID()))
                .genres(Set.of(genreFactory.createGenre("rock"), genreFactory.createGenre("pop")))
                .build();

        // when
        adapter.putMyGenres(cmd);

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
    @DisplayName("Test if putMyGenres works correctly for existing user")
    void putGenresUserExists() {
        // given
        var cmd = PutGenresCmd.builder()
                .id(new UserID(randomUUID()))
                .genres(Set.of(genreFactory.createGenre("rock"), genreFactory.createGenre("pop")))
                .build();

        // when
        userRepo.save(UserEntity.builder().id(cmd.id().value()).genres(Set.of()).build());
        adapter.putMyGenres(cmd);

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
    @DisplayName("Test if putMyGenres works correctly for existing genre")
    void putGenresGenreExists() {
        // given
        var cmd = PutGenresCmd.builder()
                .id(new UserID(randomUUID()))
                .genres(Set.of(genreFactory.createGenre("rock"), genreFactory.createGenre("pop")))
                .build();

        // when
        genreRepo.save(new GenreEntity("rock"));
        adapter.putMyGenres(cmd);

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

    private Set<GenreEntity> genresToEntity(Set<Genre> genres) {
        return genres.stream()
                .map(g -> new GenreEntity(g.value()))
                .collect(toSet());
    }
}
