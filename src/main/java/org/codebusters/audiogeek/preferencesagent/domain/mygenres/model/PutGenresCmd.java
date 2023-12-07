package org.codebusters.audiogeek.preferencesagent.domain.mygenres.model;

import lombok.Builder;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;

import java.util.Set;

@Builder
public record PutGenresCmd(UserID id, Set<Genre> genres) {
}
