package org.codebusters.audiogeek.preferencesagent.application.rest.mygenres;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.application.exception.ApiException;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.GenresException;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

/**
 * Converting Genre class to string and vice versa
 */
@Component
@RequiredArgsConstructor
@Slf4j
class GenreConverter {

    private final GenreFactory genreFactory;

    public Set<Genre> toGenres(Set<String> genres) {
        log.trace("Converting genres string to Genres, genres: {}", genres.toString());
        try {
            return genres.stream()
                    .map(genreFactory::createGenre)
                    .collect(toSet());
        } catch (GenresException err) {
            throw new ApiException(err.code(), err.message(), NOT_ACCEPTABLE);
        }
    }

    public Set<String> toString(Set<Genre> genres) {
        log.trace("Converting genres to string");
        return genres.stream()
                .map(Genre::value)
                .collect(toSet());
    }
}
