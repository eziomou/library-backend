package pl.zmudzin.library.core.application.common;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface Paginated<E> {

    Collection<E> getElements();

    Metadata getMetadata();

    static <E> Paginated<E> of(Collection<E> elements) {
        return of(elements, null);
    }

    static <E> Paginated<E> of(Collection<E> elements, Metadata metadata) {
        return new Paginated<>() {
            @Override
            public Collection<E> getElements() {
                return elements;
            }

            @Override
            public Metadata getMetadata() {
                return metadata;
            }
        };
    }

    @SafeVarargs
    static <E> Paginated<E> of(E... elements) {
        return of(List.of(elements));
    }
}
