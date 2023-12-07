package org.codebusters.audiogeek.preferencesagent.application.rest.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresQueryPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresModifyPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.GenresException;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.PutGenresCmd;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.util.Set;

import static java.nio.file.Files.readAllBytes;
import static org.codebusters.audiogeek.preferencesagent.TestErrorData.GENRE_ERROR_TEST;
import static org.codebusters.audiogeek.preferencesagent.application.exception.ApiErrorData.NOT_AUTHENTICATED;
import static org.codebusters.audiogeek.preferencesagent.application.rest.mygenres.GetMyGenresRestAdapterTest.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("MyGenresRestAdapter - /my-genres PUT")
class PutMyGenresRestAdapterTest {

    private static final String PATH_PREFIX = "src/test/resources/application/rest/mygenres/";
    private static final Path CORRECT_REQUEST = Path.of(PATH_PREFIX + "put_request_correct.json");
    private static final Path GENRE_VALIDATION_ERROR_REQUEST = Path.of(PATH_PREFIX + "put_request_genre_validation_error.json");

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MyGenresQueryPort myGenresQueryPort; // TODO remove after domain implementation
    @MockBean
    private MyGenresModifyPort myGenresModifyPort;
    @SpyBean
    private GenreFactory genreFactory;

    @Test
    @DisplayName("MyGenresRestAdapter - test if /my-genres PUT works correctly")
    void putMyGenresCorrect() throws Exception {
        // given
        Genre genre1 = genreFactory.createGenre("rock");
        Genre genre2 = genreFactory.createGenre("pop");
        doThrow(new AssertionError("Genres in body and genres given does not match"))
                .when(myGenresModifyPort)
                .putMyGenres(PutGenresCmd.builder()
                        .id(eq(TEST_ID))
                        .genres(not(eq(Set.of(genre1, genre2))))
                        .build());

        // when then
        mvc.perform(put("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .content(readAllBytes(CORRECT_REQUEST))
                        .header(AUTHORIZATION, TEST_TOKEN_CORRECT))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("MyGenresRestAdapter - test if /my-genres PUT handles exception correctly")
    void putMyGenresError() throws Exception {
        // given
        doThrow(new GenresException(GENRE_ERROR_TEST)).when(genreFactory).createGenre("BAD");

        // when then
        mvc.perform(put("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .content(readAllBytes(GENRE_VALIDATION_ERROR_REQUEST))
                        .header(AUTHORIZATION, TEST_TOKEN_CORRECT))
                .andExpect(status().is(NOT_ACCEPTABLE.value()))
                .andExpect(jsonPath("$.code", is(GENRE_ERROR_TEST.code())))
                .andExpect(jsonPath("$.message", is(GENRE_ERROR_TEST.message())));
    }

    @Test
    @DisplayName("MyGenresRestAdapter - test if /my-genres PUT authorization works")
    void putMyGenresUnauthorized() throws Exception {
        // given
        Genre genre1 = genreFactory.createGenre("rock");
        Genre genre2 = genreFactory.createGenre("pop");
        doThrow(new AssertionError("Genres in body and genres given does not match"))
                .when(myGenresModifyPort)
                .putMyGenres(PutGenresCmd.builder()
                        .id(eq(TEST_ID))
                        .genres(not(eq(Set.of(genre1, genre2))))
                        .build());

        // when then
        mvc.perform(put("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .content(readAllBytes(CORRECT_REQUEST))
                        .header(AUTHORIZATION, TEST_TOKEN_INVALID))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is(NOT_AUTHENTICATED.code())))
                .andExpect(jsonPath("$.message", is(NOT_AUTHENTICATED.message())));
    }

}
