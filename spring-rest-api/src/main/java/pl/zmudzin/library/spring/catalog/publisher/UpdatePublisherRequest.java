package pl.zmudzin.library.spring.catalog.publisher;

import javax.validation.constraints.NotNull;

public class UpdatePublisherRequest {

    @NotNull
    private String name;

    public UpdatePublisherRequest() {
    }

    public UpdatePublisherRequest(@NotNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
