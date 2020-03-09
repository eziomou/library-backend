package pl.zmudzin.library.application.catalog.publisher;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Piotr Żmudzin
 */
public class PublisherSearchRequest implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PublisherSearchRequest)) {
            return false;
        }
        PublisherSearchRequest other = (PublisherSearchRequest) obj;

        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
