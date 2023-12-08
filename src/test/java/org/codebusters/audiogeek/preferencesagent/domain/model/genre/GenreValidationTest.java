package org.codebusters.audiogeek.preferencesagent.domain.model.genre;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresQueryPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.GenreException;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.GenreException.GenreExceptionData.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Test genre validation system")
public class GenreValidationTest {
    // TODO remove after domain impl
    @MockBean
    private MyGenresQueryPort myGenresQueryPort;

    @Autowired
    private GenreFactory genreFactory;

    @Test
    @DisplayName("Test if correct genre will be created")
    void createGenreCorrect() {
        // given
        var genre = genreFactory.createGenre("rock");

        // when & then
        assertThat(genre.value()).isEqualTo("rock");
    }

    @Test
    @DisplayName("Test if too long genre will be denied")
    void createGenreTooLong() {
        // given
        var genre = "toolonggenre(verylong!!!!)";

        // when & then
        assertThatThrownBy(() -> genreFactory.createGenre(genre))
                .isInstanceOf(GenreException.class)
                .hasFieldOrPropertyWithValue("code", TOO_LONG.code())
                .hasMessage(TOO_LONG.message().formatted(genre.length(), 10));
    }

    @Test
    @DisplayName("Test if blank genre will be denied")
    void createGenreBlank() {
        // given
        var genre = "";

        // when & then
        assertThatThrownBy(() -> genreFactory.createGenre(genre))
                .isInstanceOf(GenreException.class)
                .hasFieldOrPropertyWithValue("code", BLANK.code())
                .hasMessage(BLANK.message());
    }

    @Test
    @DisplayName("Test if null genre will be denied")
    void createGenreNull() {
        assertThatThrownBy(() -> genreFactory.createGenre(null))
                .isInstanceOf(GenreException.class)
                .hasFieldOrPropertyWithValue("code", NULL.code())
                .hasMessage(NULL.message());
    }
}
