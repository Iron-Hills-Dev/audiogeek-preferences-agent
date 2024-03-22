package org.codebusters.audiogeek.preferencesagent.domain.mygenres;

import lombok.Builder;
import lombok.NonNull;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.user.UserID;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;

import java.util.Set;

@Builder
public record PutGenresCmd(@NonNull UserID id, @NonNull Set<Genre> genres) {
}
