package pl.zmudzin.library.application.catalog.genre;

import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class GenreBasicData implements Serializable {

    protected Long id;
    protected String name;

    GenreBasicData() {
    }

    public GenreBasicData(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
