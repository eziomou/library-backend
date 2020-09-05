package pl.zmudzin.library.core.application.loan;

import java.util.Optional;

public interface LoanQuery {

    Optional<String> minLoanDate();

    Optional<String> maxLoanDate();

    Optional<String> minDueDate();

    Optional<String> maxDueDate();

    Optional<Boolean> returned();
}
