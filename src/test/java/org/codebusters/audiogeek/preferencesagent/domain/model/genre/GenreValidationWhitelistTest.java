package org.codebusters.audiogeek.preferencesagent.domain.model.genre;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.GenreException;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.GenreException.GenreExceptionData.ILLEGAL_CHAR;
import static org.junit.jupiter.params.provider.Arguments.of;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "agent.genre.char-whitelist=rockp",
})
@DisplayName("Test genre validation system with whitelist provided")
public class GenreValidationWhitelistTest {
    @Autowired
    private GenreFactory genreFactory;

    @ParameterizedTest(name = "{index}. genre: {0}")
    @MethodSource("provideWrongGenres")
    @DisplayName("Test if correct genre will be created")
    void createGenreCorrect(String genreString) {
        // given
        var genre = genreFactory.createGenre(genreString);

        // when & then
        assertThat(genre.value()).isEqualTo(genreString);
    }

    @Test
    @DisplayName("Test if genre containing symbol out of the whitelist will be denied")
    void createGenreWithIllegalChar() {
        assertThatThrownBy(() -> genreFactory.createGenre("roc|"))
                .isInstanceOf(GenreException.class)
                .hasFieldOrPropertyWithValue("code", ILLEGAL_CHAR.code())
                .hasMessage(ILLEGAL_CHAR.message().formatted('|'));
    }

    public static Stream<Arguments> provideWrongGenres() {
        return Stream.of(
                of("rock"),
                of("pop")
        );
    }

}
