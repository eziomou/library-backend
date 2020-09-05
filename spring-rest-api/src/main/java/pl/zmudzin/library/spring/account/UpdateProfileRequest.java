package pl.zmudzin.library.spring.account;

import javax.validation.constraints.NotNull;

public class UpdateProfileRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(@NotNull String firstName, @NotNull String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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
