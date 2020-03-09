package pl.zmudzin.library.domain.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.util.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Piotr Żmudzin
 */
class ReservationTest {

    private static final Member MEMBER = Mockito.mock(Member.class);
    private static final Book BOOK = Mockito.mock(Book.class);
    private static final Duration DURATION = Reservation.MIN_DURATION;
    private static final LocalDateTime SUBMIT_DATE = LocalDateTime.now();

    private Reservation reservation;

    @BeforeEach
    void beforeEach() {
        try {
            reservation = createReservation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Reservation createReservation() throws Exception {
        Reservation reservation = ReflectionUtil.getInstance(Reservation.class);
        ReflectionUtil.setField(reservation, "member", MEMBER);
        ReflectionUtil.setField(reservation, "book", BOOK);
        ReflectionUtil.setField(reservation, "duration", DURATION);
        ReflectionUtil.setField(reservation, "submitDate", SUBMIT_DATE);
        ReflectionUtil.setField(reservation, "status", Reservation.Status.SUBMITTED);
        return reservation;
    }

    @Test
    void constructor_nullMember_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            try {
                getReservationConstructor().newInstance(null, BOOK, DURATION, SUBMIT_DATE);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_nullBook_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            try {
                getReservationConstructor().newInstance(MEMBER, null, DURATION, SUBMIT_DATE);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_nullDuration_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            try {
                getReservationConstructor().newInstance(MEMBER, BOOK, null, SUBMIT_DATE);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_durationShorterThaMinDuration_throwsException() {
        Duration duration = Reservation.MIN_DURATION.minusDays(1);

        assertThrows(IllegalArgumentException.class, () -> {
            try {
                getReservationConstructor().newInstance(MEMBER, BOOK, duration, SUBMIT_DATE);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_durationIsMinDuration_createsInstance() {
        Duration duration = Reservation.MIN_DURATION;

        assertDoesNotThrow(() -> getReservationConstructor().newInstance(MEMBER, BOOK, duration, SUBMIT_DATE));
    }

    @Test
    void constructor_durationLongerThaMaxDuration_throwsException() {
        Duration duration = Reservation.MAX_DURATION.plusDays(1);

        assertThrows(IllegalArgumentException.class, () -> {
            try {
                getReservationConstructor().newInstance(MEMBER, BOOK, duration, SUBMIT_DATE);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_durationIsMaxDuration_createsInstance() {
        Duration duration = Reservation.MAX_DURATION;

        assertDoesNotThrow(() -> getReservationConstructor().newInstance(MEMBER, BOOK, duration, SUBMIT_DATE));
    }

    @Test
    void constructor_nullSubmitDate_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            try {
                getReservationConstructor().newInstance(MEMBER, BOOK, DURATION, null);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> getReservationConstructor().newInstance(MEMBER, BOOK, DURATION, SUBMIT_DATE));
    }

    @Test
    void updateDuration_statusSubmitted_updatesDuration() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.SUBMITTED);

        Duration duration = Reservation.MIN_DURATION.plusDays(1);

        reservation.updateDuration(Reservation.MIN_DURATION.plusDays(1));

        assertEquals(duration, reservation.getDuration());
    }

    @Test
    void updateDuration_statusPrepared_updatesDuration() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.PREPARED);

        Duration duration = Reservation.MIN_DURATION.plusDays(1);

        reservation.updateDuration(Reservation.MIN_DURATION.plusDays(1));

        assertEquals(duration, reservation.getDuration());
    }

    @Test
    void updateDuration_statusCompleted_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.COMPLETED);

        assertThrows(IllegalStateException.class,
                () -> reservation.updateDuration(Reservation.MIN_DURATION.plusDays(1)));
    }

    @Test
    void updateDuration_statusCancelled_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.CANCELLED);

        assertThrows(IllegalStateException.class,
                () -> reservation.updateDuration(Reservation.MIN_DURATION.plusDays(1)));
    }

    @Test
    void updateDuration_statusRejected_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.REJECTED);

        assertThrows(IllegalStateException.class,
                () -> reservation.updateDuration(Reservation.MIN_DURATION.plusDays(1)));
    }

    @Test
    void submit_isSubmitted_throwsException() {
        assertThrows(IllegalStateException.class, () -> reservation.submit(LocalDateTime.now()));
    }

    @Test
    void submit_isNotSubmitted_submitsReservation() {
        ReflectionUtil.setField(reservation, "status", null);

        reservation.submit(LocalDateTime.now());

        assertEquals(reservation.getStatus(), Reservation.Status.SUBMITTED);
    }

    @Test
    void prepare_isNotSubmitted_throwsException() {
        ReflectionUtil.setField(reservation, "status", null);

        assertThrows(IllegalStateException.class, () -> reservation.prepare(LocalDateTime.now()));
    }

    @Test
    void prepare_isPrepared_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.PREPARED);

        assertThrows(IllegalStateException.class, () -> reservation.prepare(LocalDateTime.now()));
    }

    @Test
    void prepare_isCompleted_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.COMPLETED);

        assertThrows(IllegalStateException.class, () -> reservation.prepare(LocalDateTime.now()));
    }

    @Test
    void prepare_isCancelled_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.CANCELLED);

        assertThrows(IllegalStateException.class, () -> reservation.prepare(LocalDateTime.now()));
    }

    @Test
    void prepare_isRejected_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.REJECTED);

        assertThrows(IllegalStateException.class, () -> reservation.prepare(LocalDateTime.now()));
    }

    @Test
    void prepare_isSubmitted_preparesReservation() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.SUBMITTED);

        reservation.prepare(LocalDateTime.now());

        assertEquals(reservation.getStatus(), Reservation.Status.PREPARED);
    }

    @Test
    void complete_isSubmitted_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.SUBMITTED);

        assertThrows(IllegalStateException.class, () -> reservation.submit(LocalDateTime.now()));
    }

    @Test
    void complete_isCompleted_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.COMPLETED);

        assertThrows(IllegalStateException.class, () -> reservation.submit(LocalDateTime.now()));
    }

    @Test
    void complete_isCancelled_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.CANCELLED);

        assertThrows(IllegalStateException.class, () -> reservation.submit(LocalDateTime.now()));
    }

    @Test
    void complete_isRejected_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.REJECTED);

        assertThrows(IllegalStateException.class, () -> reservation.submit(LocalDateTime.now()));
    }

    @Test
    void complete_isPrepared_completesReservation() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.PREPARED);

        reservation.complete(LocalDateTime.now());

        assertEquals(reservation.getStatus(), Reservation.Status.COMPLETED);
    }

    @Test
    void cancel_isCompleted_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.COMPLETED);

        assertThrows(IllegalStateException.class, () -> reservation.cancel(LocalDateTime.now()));
    }

    @Test
    void cancel_isCancelled_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.CANCELLED);

        assertThrows(IllegalStateException.class, () -> reservation.cancel(LocalDateTime.now()));
    }

    @Test
    void cancel_isRejected_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.REJECTED);

        assertThrows(IllegalStateException.class, () -> reservation.cancel(LocalDateTime.now()));
    }

    @Test
    void cancel_isSubmitted_cancelsReservation() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.SUBMITTED);

        reservation.cancel(LocalDateTime.now());

        assertEquals(reservation.getStatus(), Reservation.Status.CANCELLED);
    }

    @Test
    void cancel_isPrepared_cancelsReservation() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.PREPARED);

        reservation.cancel(LocalDateTime.now());

        assertEquals(reservation.getStatus(), Reservation.Status.CANCELLED);
    }

    @Test
    void reject_isCompleted_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.COMPLETED);

        assertThrows(IllegalStateException.class, () -> reservation.reject(LocalDateTime.now()));
    }

    @Test
    void reject_isCancelled_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.CANCELLED);

        assertThrows(IllegalStateException.class, () -> reservation.reject(LocalDateTime.now()));
    }

    @Test
    void reject_isRejected_throwsException() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.REJECTED);

        assertThrows(IllegalStateException.class, () -> reservation.reject(LocalDateTime.now()));
    }

    @Test
    void reject_isSubmitted_cancelsReservation() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.SUBMITTED);

        reservation.reject(LocalDateTime.now());

        assertEquals(reservation.getStatus(), Reservation.Status.REJECTED);
    }

    @Test
    void reject_isPrepared_cancelsReservation() {
        ReflectionUtil.setField(reservation, "status", Reservation.Status.PREPARED);

        reservation.reject(LocalDateTime.now());

        assertEquals(reservation.getStatus(), Reservation.Status.REJECTED);
    }

    private static Constructor<Reservation> getReservationConstructor() throws NoSuchMethodException {
        return Reservation.class.getDeclaredConstructor(Member.class, Book.class, Duration.class, LocalDateTime.class);
    }
}