package org.codebusters.audiogeek.preferencesagent.application.rest.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.GetMyGenresPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.PutMyGenresPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.util.Set;

import static java.nio.file.Files.readString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

class GetMyGenresRestAdapterTest {

    private static final String PATH_PREFIX = "src/test/resources/application/rest/mygenres/";
    private static final Path CORRECT_RESPONSE = Path.of(PATH_PREFIX + "get_response_correct.json");
    private static final Path EMPTY_RESPONSE = Path.of(PATH_PREFIX + "get_response_empty.json");

    @Autowired
    private GenreFactory genreFactory;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GetMyGenresPort getMyGenresPort;
    @MockBean
    private PutMyGenresPort putMyGenresPort; // TODO remove after domain implementation

    @Test
    void getMyGenresCorrect() throws Exception {
        // given
        doReturn(Set.of(genreFactory.createGenre("rock"), genreFactory.createGenre("pop")))
                .when(getMyGenresPort).getMyGenres(any());

        // when then
        mvc.perform(get("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "dummy-token"))
                .andExpect(status().isOk())
                .andExpect(content().json(readString(CORRECT_RESPONSE)));
    }

    @Test
    void getMyGenresEmpty() throws Exception {
        // given
        doReturn(Set.of()).when(getMyGenresPort).getMyGenres(any());

        // when then
        mvc.perform(get("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "dummy-token"))
                .andExpect(status().isOk())
                .andExpect(content().json(readString(EMPTY_RESPONSE)));
    }
}