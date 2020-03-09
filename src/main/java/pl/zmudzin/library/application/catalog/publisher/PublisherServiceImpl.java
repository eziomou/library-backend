package pl.zmudzin.library.application.catalog.publisher;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.zmudzin.ddd.annotations.application.ApplicationServiceImpl;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.application.util.PredicateBuilder;
import pl.zmudzin.library.domain.catalog.Publisher;
import pl.zmudzin.library.domain.catalog.PublisherRepository;

/**
 * @author Piotr Żmudzin
 */
@ApplicationServiceImpl
public class PublisherServiceImpl implements PublisherService {

    private PublisherRepository publisherRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = {"publishers", "books"}, allEntries = true)
    @Override
    public PublisherData createPublisher(PublisherCreateRequest request) {
        Publisher publisher = new Publisher(
                request.getName()
        );
        publisher = publisherRepository.save(publisher);
        return map(publisher);
    }

    @Cacheable("publishers")
    @Override
    public PublisherData getPublisherById(Long id) {
        Publisher publisher = getPublisherEntityById(id);
        return map(publisher);
    }

    @Cacheable("publishers")
    @Override
    public Page<PublisherData> getAllPublishers(PublisherSearchRequest request, Pageable pageable) {
        Specification<Publisher> specification = (r, cq, cb) -> PredicateBuilder.builder(cb)
                .equal(r.get("name"), request.getName())
                .build();

        return publisherRepository.findAll(specification, pageable).map(this::map);
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = {"publishers", "books"}, allEntries = true)
    @Override
    public PublisherData updatePublisherById(Long id, PublisherUpdateRequest request) {
        Publisher publisher = getPublisherEntityById(id);

        if (request.getName() != null) {
            publisher.setName(request.getName());
        }
        publisher = publisherRepository.save(publisher);
        return map(publisher);
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(cacheNames = {"publishers", "books"}, allEntries = true)
    @Override
    public void deletePublisherById(Long id) {
        Publisher publisher = getPublisherEntityById(id);

        if (!publisher.getBooks().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The publisher has books");
        }
        publisherRepository.delete(publisher);
    }

    private Publisher getPublisherEntityById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private PublisherData map(Publisher publisher) {
        PublisherData data = new PublisherData();
        data.setId(publisher.getId());
        data.setName(publisher.getName());
        return data;
    }
}
