package org.codebusters.audiogeek.preferencesagent.domain.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.user.UserID;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;

import java.util.Set;

public interface MyGenresQueryPort {
    Set<Genre> getMyGenres(UserID id);
}
