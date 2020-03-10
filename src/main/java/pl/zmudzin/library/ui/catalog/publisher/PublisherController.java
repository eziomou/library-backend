package pl.zmudzin.library.ui.catalog.publisher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.application.catalog.publisher.*;
import pl.zmudzin.library.ui.util.ModelProcessorUtil;

import javax.validation.Valid;

/**
 * @author Piotr Żmudzin
 */
@RestController
@RequestMapping(path = "/publishers", produces = "application/json")
public class PublisherController {

    private PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EntityModel<PublisherData>> createPublisher(@Valid @RequestBody PublisherCreateRequest request) {
        PublisherData data = publisherService.createPublisher(request);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PublisherData>> getPublisherById(@PathVariable Long id) {
        PublisherData data = publisherService.getPublisherById(id);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PublisherData>>> getAllPublishers(@Valid PublisherSearchRequest request,
                                                                 @PageableDefault Pageable pageable) {
        Page<PublisherData> data = publisherService.getAllPublishers(request, pageable);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<EntityModel<PublisherData>> updatePublisherById(@PathVariable Long id,
                                                        @Valid @RequestBody PublisherUpdateRequest request) {
        PublisherData data = publisherService.updatePublisherById(id, request);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<PublisherData>> deletePublisherById(@PathVariable Long id) {
        publisherService.deletePublisherById(id);
        return ResponseEntity.noContent().build();
    }
}
