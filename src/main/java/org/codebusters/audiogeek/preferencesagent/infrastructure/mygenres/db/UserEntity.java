package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.EAGER;

@Setter
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "audiogeek-user")
public class UserEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToMany(cascade = PERSIST, fetch = EAGER)
    @JoinTable(name = "user-genre",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "ag_userId_fk")),
            inverseJoinColumns = @JoinColumn(name = "genre_id", foreignKey = @ForeignKey(name = "ag_genreId_fk")))
    private Set<GenreEntity> genres;
}
