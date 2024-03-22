package org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.GenreException;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.GenreException.GenreExceptionData.*;

@RequiredArgsConstructor
@Slf4j
public class GenreFactory {
    private final GenreValidationProps validationProps;

    public Genre createGenre(String genre) {
        validateGenre(genre);
        return new Genre(genre);
    }

    private void validateGenre(String genre) {
        log.trace("Validating new genre object: {}", genre);

        if (isBlank(genre)) {
            log.trace("Genre is blank");
            throw new GenreException(BLANK);
        }
        if (genre.length() > validationProps.maxLength()) {
            log.trace("Genre is too long: {}", genre.length());
            throw new GenreException(TOO_LONG, genre.length(), validationProps.maxLength());
        }
        if (!validationProps.whitelist().isBlank()) {
            validateGenreWithWhitelist(genre);
        }
        log.trace("Genre is valid");
    }

    private void validateGenreWithWhitelist(String genre) {
        genre.chars()
                .filter(c -> validationProps.whitelist().indexOf(c) == -1)
                .findFirst()
                .ifPresent(c -> {
                    log.trace("Genre contains illegal character: {}", (char) c);
                    throw new GenreException(ILLEGAL_CHAR, (char) c);
                });
    }
}
