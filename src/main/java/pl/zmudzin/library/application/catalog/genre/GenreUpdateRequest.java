package pl.zmudzin.library.application.catalog.genre;

import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class GenreUpdateRequest implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
