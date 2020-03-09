package pl.zmudzin.library.application.member;

import org.springframework.hateoas.server.core.Relation;

/**
 * @author Piotr Żmudzin
 */
@Relation(collectionRelation = "members")
public class MemberData extends MemberBasicData {
}
