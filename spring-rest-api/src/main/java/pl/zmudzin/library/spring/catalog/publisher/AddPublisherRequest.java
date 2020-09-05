package pl.zmudzin.library.spring.catalog.publisher;

import javax.validation.constraints.NotNull;

public final class AddPublisherRequest {

    @NotNull
    private String name;

    public AddPublisherRequest() {
    }

    public AddPublisherRequest(@NotNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
