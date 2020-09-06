package pl.zmudzin.library.spring.catalog.publisher;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.core.application.catalog.publisher.PublisherData;
import pl.zmudzin.library.core.application.catalog.publisher.PublisherService;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.Role;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @Transactional
    @Secured({Role.LIBRARIAN})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addPublisher(@Valid @RequestBody AddPublisherRequest request) {
        publisherService.addPublisher(request.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{publisherId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublisherData> getPublisherById(@PathVariable String publisherId) {
        PublisherData publishers = publisherService.getPublisherById(publisherId);
        return ResponseEntity.ok(publishers);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Paginated<PublisherData>> getAllPublishers(@Valid RestPublisherQuery query, @Valid RestPagination pagination) {
        Paginated<PublisherData> publishers = publisherService.getAllPublishersByQuery(query, pagination);
        return ResponseEntity.ok(publishers);
    }

    @Transactional
    @Secured({Role.LIBRARIAN})
    @PutMapping(path = "/{publisherId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePublisher(@PathVariable String publisherId, @Valid @RequestBody UpdatePublisherRequest request) {
        publisherService.updatePublisher(publisherId, request.getName());
        return ResponseEntity.ok().build();
    }

    @Transactional
    @Secured({Role.LIBRARIAN})
    @DeleteMapping(path = "/{publisherId}")
    public ResponseEntity<PublisherData> removePublisher(@PathVariable String publisherId) {
        publisherService.removePublisher(publisherId);
        return ResponseEntity.noContent().build();
    }
}
