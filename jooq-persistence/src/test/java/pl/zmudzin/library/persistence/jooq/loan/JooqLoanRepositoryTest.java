package pl.zmudzin.library.persistence.jooq.loan;

import org.junit.jupiter.api.AfterEach;
import pl.zmudzin.library.core.domain.loan.Loan;
import pl.zmudzin.library.core.domain.loan.LoanId;
import pl.zmudzin.library.core.domain.loan.LoanRepository;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepositoryTest;
import pl.zmudzin.library.persistence.jooq.PersistenceUtil;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JooqLoanRepositoryTest extends AbstractJooqRepositoryTest<LoanRepository, Loan, LoanId> {

    private final PersistenceUtil persistenceUtil;

    public JooqLoanRepositoryTest() {
        super(new JooqLoanRepository(context));
        persistenceUtil = new PersistenceUtil(context);
    }

    @Override
    protected Loan getEntity() {
        return Loan.builder()
                .id(LoanId.of(UUID.randomUUID().toString()))
                .memberId(persistenceUtil.randomMember().getId())
                .bookId(persistenceUtil.randomBook().getId())
                .loanDate(Instant.now())
                .dueDate(Instant.now().plus(Duration.ofDays(10)))
                .returned(false)
                .build();
    }

    @Override
    protected Loan getUpdatedEntity(Loan loan) {
        loan = loan.returnLoan();
        return loan;
    }

    @Override
    protected void assertEntityEquals(Loan expected, Loan result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getMemberId(), result.getMemberId());
        assertEquals(expected.getBookId(), result.getBookId());
        assertEquals(expected.getLoanDate().truncatedTo(ChronoUnit.SECONDS), result.getLoanDate().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(expected.getDueDate().truncatedTo(ChronoUnit.SECONDS), result.getDueDate().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(expected.isReturned(), result.isReturned());
    }

    @AfterEach
    @Override
    protected void afterEach() {
        super.afterEach();
        persistenceUtil.removeAll();
    }
}