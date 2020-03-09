package pl.zmudzin.library.application.catalog.genre;

import org.springframework.hateoas.server.core.Relation;

/**
 * @author Piotr Żmudzin
 */
@Relation(collectionRelation = "genres")
public class GenreData extends GenreBasicData {
}
