package pl.zmudzin.library.spring.catalog.genre;

import javax.validation.constraints.NotNull;

public final class AddGenreRequest {

    @NotNull
    private String name;

    public AddGenreRequest() {
    }

    public AddGenreRequest(@NotNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
