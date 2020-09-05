package pl.zmudzin.library.spring.catalog.author;

import pl.zmudzin.library.core.application.catalog.author.AuthorQuery;

import java.util.Objects;
import java.util.Optional;

public class RestAuthorQuery implements AuthorQuery {

    private String firstName;
    private String lastName;
    private String fullName;

    @Override
    public Optional<String> firstName() {
        return Optional.ofNullable(firstName);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public Optional<String> lastName() {
        return Optional.ofNullable(lastName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Optional<String> fullName() {
        return Optional.ofNullable(fullName);
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof RestAuthorQuery))
            return false;
        RestAuthorQuery other = (RestAuthorQuery) object;
        return Objects.equals(firstName, other.firstName) &&
                Objects.equals(lastName, other.lastName) &&
                Objects.equals(fullName, other.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, fullName);
    }
}
