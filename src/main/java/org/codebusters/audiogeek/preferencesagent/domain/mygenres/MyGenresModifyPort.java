package org.codebusters.audiogeek.preferencesagent.domain.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.PutGenresCmd;

public interface MyGenresModifyPort {
    void putMyGenres(PutGenresCmd cmd);
}
