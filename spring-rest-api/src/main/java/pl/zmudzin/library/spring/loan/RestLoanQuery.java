package pl.zmudzin.library.spring.loan;

import pl.zmudzin.library.core.application.loan.LoanQuery;

import java.util.Objects;
import java.util.Optional;

public class RestLoanQuery implements LoanQuery {

    private String minLoanDate;
    private String maxLoanDate;
    private String minDueDate;
    private String maxDueDate;
    private Boolean returned;

    @Override
    public Optional<String> minLoanDate() {
        return Optional.ofNullable(minLoanDate);
    }

    public void setMinLoanDate(String minLoanDate) {
        this.minLoanDate = minLoanDate;
    }

    @Override
    public Optional<String> maxLoanDate() {
        return Optional.ofNullable(maxLoanDate);
    }

    public void setMaxLoanDate(String maxLoanDate) {
        this.maxLoanDate = maxLoanDate;
    }

    @Override
    public Optional<String> minDueDate() {
        return Optional.ofNullable(minDueDate);
    }

    public void setMinDueDate(String minDueDate) {
        this.minDueDate = minDueDate;
    }

    @Override
    public Optional<String> maxDueDate() {
        return Optional.ofNullable(maxDueDate);
    }

    public void setMaxDueDate(String maxDueDate) {
        this.maxDueDate = maxDueDate;
    }

    @Override
    public Optional<Boolean> returned() {
        return Optional.ofNullable(returned);
    }

    public void setReturned(Boolean returned) {
        this.returned = returned;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof RestLoanQuery))
            return false;
        RestLoanQuery other = (RestLoanQuery) object;
        return Objects.equals(minLoanDate, other.minLoanDate) &&
                Objects.equals(maxLoanDate, other.maxLoanDate) &&
                Objects.equals(minDueDate, other.minDueDate) &&
                Objects.equals(maxDueDate, other.maxDueDate) &&
                Objects.equals(returned, other.returned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minLoanDate, maxLoanDate, minDueDate, maxDueDate, returned);
    }
}
