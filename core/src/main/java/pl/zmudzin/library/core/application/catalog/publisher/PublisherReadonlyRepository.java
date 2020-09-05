package pl.zmudzin.library.core.application.catalog.publisher;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;

import java.util.Optional;

public interface PublisherReadonlyRepository {

    Optional<PublisherData> findById(String publisherId);

    Paginated<PublisherData> findAllByQuery(PublisherQuery query, Pagination pagination);
}
