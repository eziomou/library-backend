package pl.zmudzin.library.application.account;

import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class AccountUpdateRequest implements Serializable {

    private String password;
    private String firstName;
    private String lastName;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
