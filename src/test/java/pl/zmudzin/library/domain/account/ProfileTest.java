package pl.zmudzin.library.domain.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Piotr Żmudzin
 */
class ProfileTest {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";

    private Profile profile;

    @BeforeEach
    void beforeEach() {
        profile = new Profile(FIRST_NAME, LAST_NAME);
    }

    @Test
    void constructor_nullFirstName_throwsException() {
        assertThrows(NullPointerException.class, () -> new Profile(null, LAST_NAME));
    }

    @Test
    void constructor_nullLastName_throwsException() {
        assertThrows(NullPointerException.class, () -> new Profile(FIRST_NAME, null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> new Profile(FIRST_NAME, LAST_NAME));
    }

    @Test
    void updateFirstName_nullFirstName_throwsException() {
        assertThrows(NullPointerException.class, () -> profile.updateFirstName(null));
    }

    @Test
    void updateFirstName_validFirstName_updatesFirstName() {
        String previousFirstName = profile.getFirstName();
        String updatedFirstName = previousFirstName + "Updated";

        profile.updateFirstName(updatedFirstName);

        assertEquals(updatedFirstName, profile.getFirstName());
    }

    @Test
    void updateLastName_nullLastName_throwsException() {
        assertThrows(NullPointerException.class, () -> profile.updateLastName(null));
    }

    @Test
    void updateLastName_validLastName_updatesLastName() {
        String previousLastName = profile.getLastName();
        String updatedLastName = previousLastName + "Updated";

        profile.updateLastName(updatedLastName);

        assertEquals(updatedLastName, profile.getLastName());
    }
}