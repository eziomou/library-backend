package pl.zmudzin.library.util;

import org.mockito.Mockito;
import pl.zmudzin.library.application.catalog.publisher.PublisherCreateRequest;
import pl.zmudzin.library.application.catalog.publisher.PublisherUpdateRequest;
import pl.zmudzin.library.domain.catalog.Publisher;
import pl.zmudzin.library.domain.catalog.PublisherRepository;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 * @author Piotr Żmudzin
 */
public class PublisherTestUtil {

    public static final Long ID = 1L;
    public static final String NAME = "name";

    public static PublisherCreateRequest getPublisherCreateRequest() {
        return getPublisherCreateRequest(NAME);
    }

    public static PublisherCreateRequest getPublisherCreateRequest(String name) {
        PublisherCreateRequest request = new PublisherCreateRequest();
        request.setName(name);
        return request;
    }

    public static PublisherUpdateRequest getPublisherUpdateRequest() {
        return getPublisherUpdateRequest(NAME + "Updated");
    }

    public static PublisherUpdateRequest getPublisherUpdateRequest(String name) {
        PublisherUpdateRequest request = new PublisherUpdateRequest();
        request.setName(name);
        return request;
    }

    public static Publisher createPublisher(PublisherRepository publisherRepository) {
        return createPublisher(publisherRepository, NAME);
    }

    public static Publisher createPublisher(PublisherRepository publisherRepository, String name) {
        Publisher publisher = new Publisher(name);
        return publisherRepository.save(publisher);
    }

    public static Publisher mockPublisher() {
        return mockPublisher(ID, NAME);
    }

    public static Publisher mockPublisher(Long id, String name) {
        Publisher publisher = Mockito.mock(Publisher.class, withSettings().lenient());
        when(publisher.getId()).thenReturn(id);
        when(publisher.getName()).thenReturn(name);
        return publisher;
    }
}
