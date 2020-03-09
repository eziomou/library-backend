package pl.zmudzin.library.application.rating;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Piotr Żmudzin
 */
public class RatingSearchRequest implements Serializable {

    private String memberUsername;
    private Long bookId;

    public String getMemberUsername() {
        return memberUsername;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RatingSearchRequest)) {
            return false;
        }
        RatingSearchRequest other = (RatingSearchRequest) obj;

        return Objects.equals(memberUsername, other.memberUsername) &&
                Objects.equals(bookId, other.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberUsername, bookId);
    }
}
