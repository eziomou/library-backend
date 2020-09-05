package pl.zmudzin.library.spring.loan;

import javax.validation.constraints.NotNull;

public final class BorrowBookRequest {

    @NotNull
    private String memberId;

    public BorrowBookRequest() {
    }

    public BorrowBookRequest(@NotNull String memberId) {
        this.memberId = memberId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
