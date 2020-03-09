package pl.zmudzin.library.application.account;

import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class AccountBasicData implements Serializable {

    protected String username;
    protected String firstName;
    protected String lastName;

    public AccountBasicData() {
    }

    public AccountBasicData(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
