package org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import static lombok.AccessLevel.PACKAGE;

@Getter
@Accessors(fluent = true)
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor(access = PACKAGE)
public final class Genre {
    private final String value;
}
