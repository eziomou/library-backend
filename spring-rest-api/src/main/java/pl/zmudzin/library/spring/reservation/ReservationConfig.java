package pl.zmudzin.library.spring.reservation;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.zmudzin.library.core.application.reservation.ReservationReadonlyRepository;
import pl.zmudzin.library.core.application.reservation.ReservationService;
import pl.zmudzin.library.core.application.reservation.ReservationServiceImpl;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.domain.loan.LoanRepository;
import pl.zmudzin.library.core.domain.member.MemberRepository;
import pl.zmudzin.library.core.domain.reservation.ReservationManager;
import pl.zmudzin.library.core.domain.reservation.ReservationManagerImpl;
import pl.zmudzin.library.core.domain.reservation.ReservationRepository;
import pl.zmudzin.library.persistence.jooq.reservation.JooqReservationRepository;

@Configuration
class ReservationConfig {

    @Bean
    JooqReservationRepository jooqReservationRepository(DSLContext context) {
        return new JooqReservationRepository(context);
    }

    @Bean
    ReservationRepository reservationRepository(JooqReservationRepository jooqReservationRepository) {
        return jooqReservationRepository;
    }

    @Bean
    ReservationReadonlyRepository reservationReadonlyRepository(JooqReservationRepository jooqReservationRepository) {
        return jooqReservationRepository;
    }

    @Bean
    ReservationManager reservationManager(ReservationRepository reservationRepository, LoanRepository loanRepository) {
        return new ReservationManagerImpl(reservationRepository, loanRepository);
    }

    @Bean
    ReservationService reservationService(MemberRepository memberRepository, BookRepository bookRepository,
                                          ReservationRepository reservationRepository, ReservationReadonlyRepository reservationReadonlyRepository,
                                          ReservationManager reservationManager) {
        return new ReservationServiceImpl(memberRepository, bookRepository, reservationRepository, reservationReadonlyRepository, reservationManager);
    }
}
