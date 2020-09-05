package pl.zmudzin.library.spring.reservation;

import javax.validation.constraints.NotNull;

public final class ReserveBookRequest {

    @NotNull
    private String bookId;

    public ReserveBookRequest() {
    }

    public ReserveBookRequest(@NotNull String bookId) {
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
