package pl.zmudzin.library.core.application.catalog.publisher;

import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.catalog.publisher.Publisher;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherId;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherRepository;
import pl.zmudzin.library.core.application.common.Pagination;

import java.util.UUID;

public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherReadonlyRepository publisherReadonlyRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository, PublisherReadonlyRepository publisherReadonlyRepository) {
        this.publisherRepository = publisherRepository;
        this.publisherReadonlyRepository = publisherReadonlyRepository;
    }

    @Override
    public void addPublisher(String name) {
        Publisher publisher = createPublisher(name);
        publisherRepository.save(publisher);
    }

    private Publisher createPublisher(String name) {
        PublisherId publisherId = new PublisherId(UUID.randomUUID().toString());
        return new Publisher(publisherId, name);
    }

    @Override
    public PublisherData getPublisherById(String publisherId) {
        return publisherReadonlyRepository.findById(publisherId).orElseThrow(() -> newPublisherNotFoundException(publisherId));
    }

    @Override
    public Paginated<PublisherData> getAllPublishersByQuery(PublisherQuery query, Pagination pagination) {
        return publisherReadonlyRepository.findAllByQuery(query, pagination);
    }

    private PublisherData map(Publisher publisher) {
        return new PublisherData(publisher.getId().toString(), publisher.getName());
    }

    @Override
    public void updatePublisher(String publisherId, String name) {
        Publisher publisher = publisherRepository.find(PublisherId.of(publisherId))
                .orElseThrow(() -> newPublisherNotFoundException(publisherId));

        publisher = publisher.withName(name);
        publisherRepository.update(publisher);
    }

    @Override
    public void removePublisher(String publisherId) {
        Publisher publisher = publisherRepository.find(PublisherId.of(publisherId))
                .orElseThrow(() -> newPublisherNotFoundException(publisherId));
        publisherRepository.delete(publisher);
    }

    private NotFoundException newPublisherNotFoundException(String publisherId) {
        return new NotFoundException("Publisher not found: " + publisherId);
    }
}
