package pl.zmudzin.library.core.domain.catalog.genre;

import pl.zmudzin.library.core.domain.common.AbstractEntity;

import java.util.Objects;

public class Genre extends AbstractEntity<GenreId> {

    private final String name;

    public Genre(GenreId id, String name) {
        super(id);
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    public Genre withName(String name) {
        return new Genre(getId(), name);
    }
}
