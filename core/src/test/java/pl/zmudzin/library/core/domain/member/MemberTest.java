package pl.zmudzin.library.core.domain.member;

import org.junit.jupiter.api.Test;
import pl.zmudzin.library.core.domain.account.Account;
import pl.zmudzin.library.core.domain.account.AccountId;
import pl.zmudzin.library.core.domain.account.Profile;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberTest {

    private final MemberId memberId = MemberId.of("");
    private final Account account = new Account(AccountId.of(""), "Foo", "Bar", new Profile("Baz", "Qux"));

    @Test
    void constructor_nullId_throwsException() {
        assertThrows(NullPointerException.class, () -> new Member(null, null));
    }

    @Test
    void constructor_nullAccount_throwsException() {
        assertThrows(NullPointerException.class, () -> new Member(memberId, null));
    }

    @Test
    void constructor_validArguments_createsInstance() {
        assertDoesNotThrow(() -> new Member(memberId, account));
    }
}