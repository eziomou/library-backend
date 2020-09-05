package pl.zmudzin.library.core.application.catalog.publisher;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;

public interface PublisherService {

    void addPublisher(String name);

    PublisherData getPublisherById(String publisherId);

    Paginated<PublisherData> getAllPublishersByQuery(PublisherQuery query, Pagination pagination);

    void updatePublisher(String publisherId, String name);

    void removePublisher(String publisherId);
}
