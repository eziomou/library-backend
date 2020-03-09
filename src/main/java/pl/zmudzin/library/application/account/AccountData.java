package pl.zmudzin.library.application.account;

import org.springframework.hateoas.server.core.Relation;

/**
 * @author Piotr Żmudzin
 */
@Relation(collectionRelation = "account")
public class AccountData extends AccountBasicData {
}
