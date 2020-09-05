package pl.zmudzin.library.core.domain.catalog.publisher;

import pl.zmudzin.library.core.domain.common.StringId;

public class PublisherId extends StringId {

    public PublisherId(String value) {
        super(value);
    }

    public static PublisherId of(String value) {
        return new PublisherId(value);
    }
}
