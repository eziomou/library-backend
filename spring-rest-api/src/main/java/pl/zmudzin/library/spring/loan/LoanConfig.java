package pl.zmudzin.library.spring.loan;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.zmudzin.library.core.application.loan.LoanReadonlyRepository;
import pl.zmudzin.library.core.application.loan.LoanService;
import pl.zmudzin.library.core.application.loan.LoanServiceImpl;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.domain.loan.LoanManager;
import pl.zmudzin.library.core.domain.loan.LoanManagerImpl;
import pl.zmudzin.library.core.domain.loan.LoanRepository;
import pl.zmudzin.library.core.domain.member.MemberRepository;
import pl.zmudzin.library.core.domain.reservation.ReservationManager;
import pl.zmudzin.library.persistence.jooq.loan.JooqLoanRepository;

@Configuration
class LoanConfig {

    @Bean
    JooqLoanRepository jooqLoanRepository(DSLContext context) {
        return new JooqLoanRepository(context);
    }

    @Bean
    LoanManager loanManager(ReservationManager reservationManager, LoanRepository loanRepository) {
        return new LoanManagerImpl(reservationManager, loanRepository);
    }

    @Bean
    LoanService loanService(MemberRepository memberRepository, BookRepository bookRepository, LoanManager loanManager,
                            LoanReadonlyRepository loanReadonlyRepository) {
        return new LoanServiceImpl(memberRepository, bookRepository, loanManager, loanReadonlyRepository);
    }
}
