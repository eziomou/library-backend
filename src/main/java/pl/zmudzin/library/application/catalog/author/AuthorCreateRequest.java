package pl.zmudzin.library.application.catalog.author;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class AuthorCreateRequest implements Serializable {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

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
