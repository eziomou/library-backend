package pl.zmudzin.library.domain.rating;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.zmudzin.library.domain.catalog.Book;
import pl.zmudzin.library.domain.member.Member;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Piotr Żmudzin
 */
class RatingTest {

    private static final Member MEMBER = Mockito.mock(Member.class);
    private static final Book BOOK = Mockito.mock(Book.class);
    private static final int VALUE = Rating.MAX_VALUE;
    private static final LocalDateTime RATE_DATE = LocalDateTime.now();

    @Test
    void constructor_nullMember_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            try {
                getRatingConstructor().newInstance(null, BOOK, VALUE, RATE_DATE);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_nullBook_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            try {
                getRatingConstructor().newInstance(MEMBER, null, VALUE, RATE_DATE);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_valueLowerThanMinValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                getRatingConstructor().newInstance(MEMBER, BOOK, Rating.MIN_VALUE - 1, RATE_DATE);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_valueGreaterThanMaxValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                getRatingConstructor().newInstance(MEMBER, BOOK, Rating.MAX_VALUE + 1, RATE_DATE);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_nullRateDate_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            try {
                getRatingConstructor().newInstance(MEMBER, BOOK, VALUE, null);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> getRatingConstructor().newInstance(MEMBER, BOOK, VALUE, RATE_DATE));
    }

    private static Constructor<Rating> getRatingConstructor() throws NoSuchMethodException {
        return Rating.class.getDeclaredConstructor(Member.class, Book.class, int.class, LocalDateTime.class);
    }
}