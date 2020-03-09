package pl.zmudzin.library.application.member;

import pl.zmudzin.library.application.util.PersonSearchRequest;

/**
 * @author Piotr Żmudzin
 */
public class MemberSearchRequest extends PersonSearchRequest {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
