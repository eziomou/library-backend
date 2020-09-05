package pl.zmudzin.library.core.domain.catalog.publisher;

import pl.zmudzin.library.core.domain.common.AbstractEntity;

import java.util.Objects;

public class Publisher extends AbstractEntity<PublisherId> {

    private final String name;

    public Publisher(PublisherId id, String name) {
        super(id);
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    public Publisher withName(String name) {
        return new Publisher(getId(), name);
    }
}
