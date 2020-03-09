package pl.zmudzin.library.ui.reservation;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import pl.zmudzin.library.application.reservation.ReservationData;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Piotr Żmudzin
 */
@Component
public class ReservationModelProcessor implements RepresentationModelProcessor<EntityModel<ReservationData>> {

    @Override
    public EntityModel<ReservationData> process(EntityModel<ReservationData> model) {
        model.add(createReservationLink(model.getContent().getId()));
        return model;
    }

    private static Link createReservationLink(Long reservationId) {
        return linkTo(methodOn(ReservationController.class).getReservationById(reservationId)).withSelfRel();
    }
}
