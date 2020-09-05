package pl.zmudzin.library.persistence.jooq.reservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.zmudzin.library.core.domain.catalog.book.Book;
import pl.zmudzin.library.core.domain.member.Member;
import pl.zmudzin.library.core.domain.reservation.Reservation;
import pl.zmudzin.library.core.domain.reservation.ReservationId;
import pl.zmudzin.library.core.domain.reservation.ReservationRepository;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepositoryTest;
import pl.zmudzin.library.persistence.jooq.PersistenceUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JooqReservationRepositoryTest extends AbstractJooqRepositoryTest<ReservationRepository, Reservation, ReservationId> {

    private final PersistenceUtil persistenceUtil;

    protected JooqReservationRepositoryTest() {
        super(new JooqReservationRepository(context));
        persistenceUtil = new PersistenceUtil(context);
    }

    @Override
    protected Reservation getEntity() {
        return new Reservation(
                ReservationId.of(UUID.randomUUID().toString()),
                persistenceUtil.randomMember().getId(),
                persistenceUtil.randomBook().getId(),
                Instant.now()
        );
    }

    @Override
    protected Reservation getUpdatedEntity(Reservation reservation) {
        reservation = reservation.prepare(Instant.now());
        return reservation;
    }

    @Override
    protected void assertEntityEquals(Reservation expected, Reservation result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getMemberId(), result.getMemberId());
        assertEquals(expected.getBookId(), result.getBookId());
        assertEquals(expected.getStatus(), result.getStatus());
    }

    @Test
    void findFirstSubmitted_ThreeReservations_returnsFirst() {
        Member member = persistenceUtil.randomMember();
        Book book = persistenceUtil.randomBook();
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Reservation reservation = new Reservation(ReservationId.of(UUID.randomUUID().toString()),
                    member.getId(), book.getId(), Instant.now().plus(Duration.ofDays(i)));
            reservations.add(reservation);
            repository.save(reservation);
        }
        repository.findFirstSubmitted(book.getId()).ifPresentOrElse(r -> assertEntityEquals(reservations.get(0), r),
                Assertions::fail);
    }
}