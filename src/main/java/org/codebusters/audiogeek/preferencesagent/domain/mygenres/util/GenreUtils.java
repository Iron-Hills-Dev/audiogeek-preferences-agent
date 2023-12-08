package org.codebusters.audiogeek.preferencesagent.domain.mygenres.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.application.exception.ApiException;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.GenreException;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.GenreEntity;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreUtils {

    private final GenreFactory genreFactory;

    public Set<Genre> stringsToGenres(Set<String> genres) {
        log.trace("Converting genres string to Genres, genres: {}", genres.toString());
        try {
            return genres.stream()
                    .map(genreFactory::createGenre)
                    .collect(toSet());
        } catch (GenreException err) {
            throw new ApiException(err.code(), err.message(), NOT_ACCEPTABLE);
        }
    }

    public Set<String> genresToStrings(Set<Genre> genres) {
        log.trace("Converting genres to string");
        return genres.stream()
                .map(Genre::value)
                .collect(toSet());
    }

    public Set<Genre> entityToGenres(Set<GenreEntity> genres) {
        return genres.stream()
                .map(g -> genreFactory.createGenre(g.getName()))
                .collect(toSet());
    }
}
