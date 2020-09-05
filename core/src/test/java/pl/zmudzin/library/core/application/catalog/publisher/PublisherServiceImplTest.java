package pl.zmudzin.library.core.application.catalog.publisher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.domain.catalog.publisher.Publisher;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherId;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private PublisherReadonlyRepository publisherReadonlyRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    @Test
    void addPublisher_validPublisher_addsPublisher() {
        publisherService.addPublisher("Foo");

        verify(publisherRepository, times(1)).save(any());
    }

    @Test
    void updatePublisher_existingPublisher_updatesPublisher() {
        Publisher publisher = new Publisher(PublisherId.of(""), "Foo");

        when(publisherRepository.find(publisher.getId())).thenReturn(Optional.of(publisher));

        publisherService.updatePublisher(publisher.getId().toString(), "Bar");

        verify(publisherRepository, times(1)).update(any());
    }

    @Test
    void updatePublisher_notExistingPublisher_throwsException() {
        PublisherId publisherId = PublisherId.of("");

        when(publisherRepository.find(publisherId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> publisherService.updatePublisher(publisherId.toString(), "Foo"));

        verify(publisherRepository, times(0)).update(any());
    }

    @Test
    void removePublisher_existingPublisher_deletesPublisher() {
        Publisher publisher = new Publisher(PublisherId.of(""), "Foo");

        when(publisherRepository.find(publisher.getId())).thenReturn(Optional.of(publisher));

        publisherService.removePublisher(publisher.getId().toString());

        verify(publisherRepository, times(1)).delete(any());
    }

    @Test
    void removePublisher_notExistingPublisher_throwsException() {
        PublisherId publisherId = PublisherId.of("");

        when(publisherRepository.find(publisherId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> publisherService.removePublisher(publisherId.toString()));

        verify(publisherRepository, times(0)).delete(any());
    }
}