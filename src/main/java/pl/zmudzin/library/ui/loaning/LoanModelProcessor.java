package pl.zmudzin.library.ui.loaning;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import pl.zmudzin.library.application.loaning.LoanData;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Piotr Żmudzin
 */
@Component
public class LoanModelProcessor implements RepresentationModelProcessor<EntityModel<LoanData>> {

    @Override
    public EntityModel<LoanData> process(EntityModel<LoanData> model) {
        model.add(createLoanLink(model.getContent().getId()));
        return model;
    }

    private static Link createLoanLink(Long loanId) {
        return linkTo(methodOn(LoaningController.class).getLoanById(loanId)).withSelfRel();
    }
}
