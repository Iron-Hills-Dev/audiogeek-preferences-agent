package org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre;

public class GenreFactory {
    public Genre createGenre(String genre) {
        // TODO validate input
        return new Genre(genre);
    }
}
