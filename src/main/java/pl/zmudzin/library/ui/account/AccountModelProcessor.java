package pl.zmudzin.library.ui.account;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import pl.zmudzin.library.application.account.AccountData;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Piotr Żmudzin
 */
@Component
public class AccountModelProcessor implements RepresentationModelProcessor<EntityModel<AccountData>> {

    @Override
    public EntityModel<AccountData> process(EntityModel<AccountData> model) {
        model.add(createAccountLink(model.getContent().getUsername(), "self"));
        return model;
    }

    public static Link createAccountLink(String username, String rel) {
        return linkTo(methodOn(AccountController.class).getAccountByUsername(username)).withRel(rel);
    }
}
