package pl.zmudzin.library.spring.catalog.genre;

import pl.zmudzin.library.core.application.catalog.genre.GenreQuery;

import java.util.Objects;
import java.util.Optional;

public class RestGenreQuery implements GenreQuery {

    private String name;

    @Override
    public Optional<String> name() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof RestGenreQuery))
            return false;
        RestGenreQuery other = (RestGenreQuery) object;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
