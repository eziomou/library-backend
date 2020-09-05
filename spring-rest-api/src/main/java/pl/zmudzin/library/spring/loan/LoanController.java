package pl.zmudzin.library.spring.loan;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.loan.LoanData;
import pl.zmudzin.library.core.application.loan.LoanService;
import pl.zmudzin.library.spring.catalog.book.BookController;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.AuthorizationService;
import pl.zmudzin.library.spring.security.Role;

import javax.validation.Valid;

@RestController
@RequestMapping
public class LoanController {

    private final LoanService loanService;
    private final AuthorizationService authorizationService;

    public LoanController(LoanService loanService, AuthorizationService authorizationService) {
        this.loanService = loanService;
        this.authorizationService = authorizationService;
    }

    @Secured({Role.LIBRARIAN})
    @PostMapping(path = BookController.BASE_PATH + "/{bookId}/loans", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> borrowBook(@PathVariable String bookId, @Valid @RequestBody BorrowBookRequest request) {
        loanService.borrowBook(request.getMemberId(), bookId);
        return ResponseEntity.ok().build();
    }

    @Secured({Role.LIBRARIAN})
    @PutMapping(path = BookController.BASE_PATH + "/{bookId}/loans")
    public ResponseEntity<?> returnBook(@PathVariable String bookId) {
        loanService.returnBook(bookId);
        return ResponseEntity.ok().build();
    }

    @Secured({Role.LIBRARIAN})
    @GetMapping(path = "/loans")
    public ResponseEntity<?> getLoans(@Valid RestLoanQuery query, @Valid RestPagination pagination) {
        Paginated<LoanData> loans = loanService.getAllLoans(query, pagination);
        return ResponseEntity.ok(loans);
    }

    @Secured({Role.LIBRARIAN})
    @GetMapping(path = "/loans/{loanId}")
    public ResponseEntity<?> getLoan(@PathVariable String loanId) {
        LoanData loan = loanService.getLoan(loanId);
        return ResponseEntity.ok(loan);
    }

    @Secured({Role.LIBRARIAN})
    @GetMapping(path = "/members/{memberId}/loans")
    public ResponseEntity<?> getMemberLoans(@PathVariable String memberId, @Valid RestLoanQuery query, @Valid RestPagination pagination) {
        Paginated<LoanData> loans = loanService.getAllMemberLoans(memberId, query, pagination);
        return ResponseEntity.ok(loans);
    }

    @Secured({Role.LIBRARIAN})
    @GetMapping(path = "/books/{bookId}/loans")
    public ResponseEntity<?> getBookLoans(@PathVariable String bookId, @Valid RestLoanQuery query, @Valid RestPagination pagination) {
        Paginated<LoanData> loans = loanService.getAllBookLoans(bookId, query, pagination);
        return ResponseEntity.ok(loans);
    }

    @Secured({Role.MEMBER})
    @GetMapping(path = "/member/loans")
    public ResponseEntity<?> getMemberLoans(@Valid RestLoanQuery query, @Valid RestPagination pagination) {
        Paginated<LoanData> loans = loanService.getAllMemberLoans(authorizationService.getMemberId(), query, pagination);
        return ResponseEntity.ok(loans);
    }

    @Secured({Role.MEMBER})
    @GetMapping(path = "/member/loans/{loanId}")
    public ResponseEntity<?> getMemberLoan(@PathVariable String loanId) {
        LoanData loan = loanService.getMemberLoan(authorizationService.getMemberId(), loanId);
        return ResponseEntity.ok(loan);
    }
}
