package pl.zmudzin.library.application.loaning;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.zmudzin.ddd.annotations.application.ApplicationServiceImpl;
import pl.zmudzin.library.application.catalog.book.BookBasicData;
import pl.zmudzin.library.application.member.MemberBasicData;
import pl.zmudzin.library.application.security.AuthenticationService;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.loan.Loan;
import pl.zmudzin.library.domain.loan.LoanDomainService;
import pl.zmudzin.library.domain.loan.LoanRepository;
import pl.zmudzin.library.domain.reservation.Reservation;
import pl.zmudzin.library.domain.reservation.ReservationRepository;

/**
 * @author Piotr Żmudzin
 */
@ApplicationServiceImpl
public class LoanServiceImpl implements LoanService {

    private LoanDomainService loanDomainService;
    private LoanRepository loanRepository;
    private ReservationRepository reservationRepository;
    private AuthenticationService authenticationService;

    public LoanServiceImpl(LoanDomainService loanDomainService, LoanRepository loanRepository,
                           ReservationRepository reservationRepository, AuthenticationService authenticationService) {
        this.loanDomainService = loanDomainService;
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
        this.authenticationService = authenticationService;
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public LoanData createLoan(LoanCreateRequest request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Loan loan = loanDomainService.loan(reservation);
        loan = loanRepository.save(loan);
        return map(loan);
    }

    @Secured({Roles.MEMBER, Roles.LIBRARIAN})
    @Override
    public LoanData getLoanById(Long id) {
        Loan loan = getLoanEntityById(id);
        return map(loan);
    }

    @Secured({Roles.MEMBER, Roles.LIBRARIAN})
    @Override
    public Page<LoanData> getAllLoans(LoanSearchRequest request, Pageable pageable) {
        Specification<Loan> specification = (r, cq, cb) -> {
            LoanPredicateBuilder builder = LoanPredicateBuilder.builder(r, cb);

            if (authenticationService.hasAuthority(Roles.LIBRARIAN)) {
                builder
                        .memberUsername(request.getMemberUsername())
                        .memberFullName(request.getMemberFullName());
            } else {
                builder
                        .memberUsername(authenticationService.getUsername());
            }
            builder
                    .bookId(request.getBookId())
                    .bookTitle(request.getBookTitle())
                    .completed(request.getCompleted());

            return builder.build();
        };
        return loanRepository.findAll(specification, pageable).map(this::map);
    }

    @Secured(Roles.LIBRARIAN)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public LoanData updateLoanById(Long id, LoanUpdateRequest request) {
        Loan loan = getLoanEntityById(id);

        if (request.getCompleted() != null && request.getCompleted()) {
            loanDomainService.ret(loan);
        }
        loan = loanRepository.save(loan);
        return map(loan);
    }

    private Loan getLoanEntityById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!authenticationService.isResourceOwner(loan) &&
                !authenticationService.hasAuthority(Roles.LIBRARIAN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return loan;
    }

    public LoanData map(Loan loan) {
        LoanData data = new LoanData();
        data.setId(loan.getId());

        data.setMember(new MemberBasicData(
                loan.getMember().getAccount().getUsername(),
                loan.getMember().getAccount().getProfile().getFirstName(),
                loan.getMember().getAccount().getProfile().getLastName()
        ));
        data.setBook(new BookBasicData(
                loan.getBook().getId(),
                loan.getBook().getTitle()
        ));
        data.setLoanDate(loan.getLoanDate());
        data.setDueDate(loan.getDueDate());
        data.setCompleted(loan.isCompleted());
        data.setCompleteDate(loan.getCompleteDate());
        return data;
    }
}
