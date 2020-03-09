package pl.zmudzin.library.application.catalog.genre;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Piotr Żmudzin
 */
public class GenreSearchRequest implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GenreSearchRequest)) {
            return false;
        }
        GenreSearchRequest other = (GenreSearchRequest) obj;

        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
