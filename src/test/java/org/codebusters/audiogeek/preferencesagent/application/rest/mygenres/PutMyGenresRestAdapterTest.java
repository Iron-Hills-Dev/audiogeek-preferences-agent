package org.codebusters.audiogeek.preferencesagent.application.rest.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.GetMyGenresPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.PutMyGenresPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.MyGenresDomainException;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.util.Set;

import static java.nio.file.Files.readAllBytes;
import static org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.MyGenresDomainErrorData.GENRE_ERROR_TEST;
import static org.hamcrest.Matchers.is;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PutMyGenresRestAdapterTest {

    private static final String PATH_PREFIX = "src/test/resources/application/rest/mygenres/";
    private static final Path CORRECT_REQUEST = Path.of(PATH_PREFIX + "put_request_correct.json");
    private static final Path GENRE_VALIDATION_ERROR_REQUEST = Path.of(PATH_PREFIX + "put_request_genre_validation_error.json");

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GetMyGenresPort getMyGenresPort; // TODO remove after domain implementation
    @MockBean
    private PutMyGenresPort putMyGenresPort;
    @SpyBean
    private GenreFactory genreFactory;

    @Test
    void putMyGenresCorrect() throws Exception {
        // given
        Genre genre1 = genreFactory.createGenre("rock");
        Genre genre2 = genreFactory.createGenre("pop");
        doThrow(new AssertionError("Genres in body and genres given does not match"))
                .when(putMyGenresPort)
                .putMyGenres(any(), not(eq(Set.of(genre1, genre2))));

        // when then
        mvc.perform(put("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .content(readAllBytes(CORRECT_REQUEST))
                        .header(AUTHORIZATION, "dummy-token"))
                .andExpect(status().isCreated());
    }

    @Test
    void putMyGenresError() throws Exception {
        // given
        var err = GENRE_ERROR_TEST;
        doThrow(new MyGenresDomainException(GENRE_ERROR_TEST, "test_param")).when(genreFactory).createGenre("BAD");

        // when then
        mvc.perform(put("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .content(readAllBytes(GENRE_VALIDATION_ERROR_REQUEST))
                        .header(AUTHORIZATION, "dummy-token"))
                .andExpect(status().is(err.getHttpStatus().value()))
                .andExpect(jsonPath("$.code", is(err.getCode())))
                .andExpect(jsonPath("$.message", is(err.getMessage().formatted("test_param"))));
    }

}
