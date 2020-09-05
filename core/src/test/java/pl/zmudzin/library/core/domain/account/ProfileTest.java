package pl.zmudzin.library.core.domain.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    @Test
    void constructor_nullFirstName_throwsException() {
        assertThrows(NullPointerException.class, () -> new Profile(null, "Foo"));
    }

    @Test
    void constructor_nullLastName_throwsException() {
        assertThrows(NullPointerException.class, () -> new Profile("Foo", null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        Profile profile = new Profile("Foo", "Bar");
        assertEquals("Foo", profile.getFirstName());
        assertEquals("Bar", profile.getLastName());
        assertEquals("Foo Bar", profile.getFullName());
    }

    @Test
    void equals_sameProfile_returnsTrue() {
        Profile first = new Profile("Foo", "Bar");
        Profile second = new Profile("Foo", "Bar");
        assertEquals(first, second);
    }

    @Test
    void equals_differentFirstName_returnsFalse() {
        Profile first = new Profile("Foo", "Bar");
        Profile second = new Profile("Baz", "Bar");
        assertNotEquals(first, second);
    }

    @Test
    void equals_differentLastName_returnsFalse() {
        Profile first = new Profile("Foo", "Bar");
        Profile second = new Profile("Foo", "Baz");
        assertNotEquals(first, second);
    }
}