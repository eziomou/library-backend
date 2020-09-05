package pl.zmudzin.library.spring.catalog.genre;

import javax.validation.constraints.NotNull;

public class UpdateGenreRequest {

    @NotNull
    private String name;

    public UpdateGenreRequest() {
    }

    public UpdateGenreRequest(@NotNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
