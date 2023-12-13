package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.UserNotFoundException;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.PutGenresCmd;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.UserID;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class JpaMyGenresQueryAdapterTest {
    @Autowired
    private JpaMyGenresModifyAdapter modifyAdapter;
    @Autowired
    private JpaMyGenresQueryAdapter queryAdapter;
    @Autowired
    private GenreRepository genreRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private GenreFactory genreFactory;


    @BeforeEach
    void clearDatabase() {
        userRepo.deleteAll();
        genreRepo.deleteAll();
    }

    @Test
    @DisplayName("Test if getMyGenres works correctly")
    void getGenresCorrect() {
        // given
        var genre = genreFactory.createGenre("rock");
        var cmd = PutGenresCmd.builder()
                .id(new UserID(randomUUID()))
                .genres(Set.of(genre))
                .build();

        // when
        modifyAdapter.putMyGenres(cmd);

        // then
        assertThat(queryAdapter.getMyGenres(cmd.id())).contains(genre);
    }

    @Test
    @DisplayName("Test if getMyGenres handles not existing user")
    void getGenresDoNotExists() {
        assertThatThrownBy(() -> queryAdapter.getMyGenres(new UserID(randomUUID())))
                .isInstanceOf(UserNotFoundException.class);
    }
}
