package pl.zmudzin.library.application.catalog.author;

import org.springframework.hateoas.server.core.Relation;

/**
 * @author Piotr Żmudzin
 */
@Relation(collectionRelation = "authors")
public class AuthorData extends AuthorBasicData {
}
