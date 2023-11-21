package org.codebusters.audiogeek.preferencesagent.domain.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.UserID;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;

import java.util.Set;

public interface GetMyGenresPort {
    Set<Genre> getMyGenres(UserID id);
}
