package pl.zmudzin.library.application.catalog.publisher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.zmudzin.ddd.annotations.application.ApplicationService;

/**
 * @author Piotr Żmudzin
 */
@ApplicationService
public interface PublisherService {

    PublisherData createPublisher(PublisherCreateRequest request);

    PublisherData getPublisherById(Long id);

    Page<PublisherData> getAllPublishers(PublisherSearchRequest request, Pageable pageable);

    PublisherData updatePublisherById(Long id, PublisherUpdateRequest request);

    void deletePublisherById(Long id);
}
