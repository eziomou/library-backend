package pl.zmudzin.library.core.application.account;

public class ProfileData {

    private final String firstName;
    private final String lastName;

    public ProfileData(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
