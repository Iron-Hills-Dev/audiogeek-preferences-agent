package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Entity
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Table(
        name = "genre",
        uniqueConstraints = {@UniqueConstraint(name = "g_name_uk", columnNames = "name")}
)
public class GenreEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    @EqualsAndHashCode.Exclude
    private UUID id;

    @Setter
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    public GenreEntity(String name) {
        this.name = name;
    }
}
