package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresQueryPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.PutGenresCmd;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.UserID;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.GenreEntity;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.UserEntity;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.repo.GenreRepository;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class JpaMyGenresModifyAdapterTest {
    @MockBean
    private MyGenresQueryPort myGenresQueryPort; // TODO remove after domain implementation
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
        genreRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void putGenresCorrect() {
        // given
        var cmd = PutGenresCmd.builder()
                .id(new UserID(randomUUID()))
                .genres(Set.of(genreFactory.createGenre("rock"), genreFactory.createGenre("pop")))
                .build();

        // when
        adapter.putMyGenres(cmd);

        // then
        var exceptedEntity = UserEntity.builder()
                .id(cmd.id().id())
                .genres(genresToEntity(cmd.genres()))
                .build();

        var albumEntity = userRepo.findById(cmd.id().id());
        assertThat(albumEntity)
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
