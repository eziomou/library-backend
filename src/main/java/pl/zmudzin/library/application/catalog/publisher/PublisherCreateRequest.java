package pl.zmudzin.library.application.catalog.publisher;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class PublisherCreateRequest implements Serializable {

    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
