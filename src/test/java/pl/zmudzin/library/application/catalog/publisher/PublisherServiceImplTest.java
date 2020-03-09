package pl.zmudzin.library.application.catalog.publisher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.domain.catalog.Publisher;
import pl.zmudzin.library.domain.catalog.PublisherRepository;
import pl.zmudzin.library.util.PublisherTestUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Piotr Żmudzin
 */
@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    @Test
    void createPublisher_validArguments_createsPublisher() {
        PublisherCreateRequest request = PublisherTestUtil.getPublisherCreateRequest();
        Publisher publisher = PublisherTestUtil.mockPublisher();

        when(publisherRepository.save(any())).thenReturn(publisher);

        PublisherData data = publisherService.createPublisher(request);

        verify(publisherRepository, times(1)).save(any());

        assertPublisherEquals(publisher, data);
    }

    @Test
    void updatePublisherById_validArguments_updatesPublisher() {
        PublisherUpdateRequest request = PublisherTestUtil.getPublisherUpdateRequest();
        Publisher publisher = PublisherTestUtil.mockPublisher();
        Publisher updatedPublisher = PublisherTestUtil.mockPublisher(1L, request.getName());

        when(publisher.getId()).thenReturn(1L);
        when(publisherRepository.findById(publisher.getId())).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(any())).thenReturn(updatedPublisher);

        PublisherData data = publisherService.updatePublisherById(publisher.getId(), request);

        verify(publisher, times(1)).setName(request.getName());
        verify(publisherRepository, times(1)).save(any());

        assertPublisherEquals(updatedPublisher, data);
    }

    @Test
    void deletePublisherById_existingPublisher_deletesPublisher() {
        Publisher publisher = PublisherTestUtil.mockPublisher();

        when(publisher.getId()).thenReturn(1L);
        when(publisherRepository.findById(publisher.getId())).thenReturn(Optional.of(publisher));

        publisherService.deletePublisherById(publisher.getId());

        verify(publisherRepository, times(1)).delete(publisher);
    }

    private static void assertPublisherEquals(Publisher publisher, PublisherData data) {
        assertEquals(publisher.getName(), data.getName());
    }
}