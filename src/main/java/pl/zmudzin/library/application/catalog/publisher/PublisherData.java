package pl.zmudzin.library.application.catalog.publisher;

import org.springframework.hateoas.server.core.Relation;

/**
 * @author Piotr Żmudzin
 */
@Relation(collectionRelation = "publishers")
public class PublisherData extends PublisherBasicData {
}
