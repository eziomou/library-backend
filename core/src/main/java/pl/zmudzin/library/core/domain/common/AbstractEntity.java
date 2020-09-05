package pl.zmudzin.library.core.domain.common;

import java.util.Objects;

public abstract class AbstractEntity<ID> implements Entity<ID> {

    private final ID id;

    public AbstractEntity(ID id) {
        this.id = Objects.requireNonNull(id);
    }

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof AbstractEntity))
            return false;
        AbstractEntity<?> other = (AbstractEntity<?>) object;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
