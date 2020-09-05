package pl.zmudzin.library.core.domain.loan;

import pl.zmudzin.library.core.domain.common.StringId;

public class LoanId extends StringId {

    public LoanId(String value) {
        super(value);
    }

    public static LoanId of(String value) {
        return new LoanId(value);
    }
}
