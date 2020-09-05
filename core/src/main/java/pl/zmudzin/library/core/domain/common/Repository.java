package pl.zmudzin.library.core.domain.common;

import java.util.Collection;
import java.util.Optional;

public interface Repository<E extends Entity<ID>, ID> {

    int size();

    void save(E entity);

    void saveAll(Collection<E> entities);

    boolean exists(ID id);

    Optional<E> find(ID id);

    void update(E entity);

    void delete(E entity);

    void deleteAll();
}
