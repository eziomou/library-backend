package pl.zmudzin.library.spring.security;

public final class Role {

    private static final String PREFIX = "ROLE_";

    public static final String MEMBER = PREFIX + WithoutPrefix.MEMBER;
    public static final String LIBRARIAN = PREFIX + WithoutPrefix.LIBRARIAN;

    private Role() {
    }

    public static final class WithoutPrefix {

        public static final String MEMBER = "MEMBER";
        public static final String LIBRARIAN = "LIBRARIAN";

        private WithoutPrefix() {
        }
    }
}
