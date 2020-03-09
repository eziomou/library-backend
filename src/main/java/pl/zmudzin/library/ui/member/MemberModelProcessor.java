package pl.zmudzin.library.ui.member;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import pl.zmudzin.library.application.member.MemberData;
import pl.zmudzin.library.ui.loaning.LoaningController;
import pl.zmudzin.library.ui.reservation.ReservationController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Piotr Żmudzin
 */
@Component
public class MemberModelProcessor implements RepresentationModelProcessor<EntityModel<MemberData>> {

    @Override
    public EntityModel<MemberData> process(EntityModel<MemberData> model) {
        model.add(createMemberLink(model.getContent().getUsername(), "self"));
        model.add(createReservationsLink(model.getContent().getUsername()));
        model.add(createLoansLink(model.getContent().getUsername()));
        return model;
    }

    public static Link createMemberLink(String username, String rel) {
        return linkTo(methodOn(MemberController.class).getMemberByUsername(username)).withRel(rel);
    }

    private static Link createReservationsLink(String username) {
        UriComponents components = linkTo(ReservationController.class).toUriComponentsBuilder()
                .query("memberUsername={username}").buildAndExpand(username);

        return new Link(components.toString(), "reservations");
    }

    private static Link createLoansLink(String username) {
        UriComponents components = linkTo(LoaningController.class).toUriComponentsBuilder()
                .query("memberUsername={username}").buildAndExpand(username);

        return new Link(components.toString(), "loans");
    }
}
