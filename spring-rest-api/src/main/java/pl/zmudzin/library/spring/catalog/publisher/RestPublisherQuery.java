package pl.zmudzin.library.spring.catalog.publisher;

import pl.zmudzin.library.core.application.catalog.publisher.PublisherQuery;

import java.util.Objects;
import java.util.Optional;

public class RestPublisherQuery implements PublisherQuery {

    private String name;

    @Override
    public Optional<String> name() {
        return Optional.of(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof RestPublisherQuery))
            return false;
        RestPublisherQuery other = (RestPublisherQuery) object;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
