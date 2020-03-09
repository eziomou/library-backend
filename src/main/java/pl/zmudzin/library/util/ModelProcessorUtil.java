package pl.zmudzin.library.util;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Piotr Żmudzin
 */
public class ModelProcessorUtil {

    public static <T> ResponseEntity<EntityModel<T>> toResponseEntity(T t) {
        return toResponseEntity(t, HttpStatus.OK);
    }

    public static <T> ResponseEntity<EntityModel<T>> toResponseEntity(T t, HttpStatus status) {
        return new ResponseEntity<>(new EntityModel<>(t), status);
    }

    public static <T> ResponseEntity<PagedModel<EntityModel<T>>> toResponseEntity(Page<T> data) {
        return toResponseEntity(data, HttpStatus.OK);
    }

    private static <T> ResponseEntity<PagedModel<EntityModel<T>>> toResponseEntity(Page<T> data, HttpStatus status) {
        PagedModel<EntityModel<T>> model = toPagedModel(data);
        return new ResponseEntity<>(model, status);
    }

    private static <T> PagedModel<EntityModel<T>> toPagedModel(Page<T> page) {
        return new PagedModel<>(page.map(e -> new EntityModel<>(e)).getContent(), toPageMetadata(page));
    }

    private static <T> PagedModel.PageMetadata toPageMetadata(Page<T> page) {
        return new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
    }
}
