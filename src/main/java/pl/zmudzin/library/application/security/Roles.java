package pl.zmudzin.library.application.security;

/**
 * @author Piotr Żmudzin
 */
public class Roles {

    public static final String PREFIX = "ROLE_";

    public static final String MEMBER = PREFIX + WithoutPrefix.MEMBER;
    public static final String LIBRARIAN = PREFIX + WithoutPrefix.LIBRARIAN;

    public static class WithoutPrefix {

        public static final String MEMBER = "MEMBER";
        public static final String LIBRARIAN = "LIBRARIAN";
    }
}
