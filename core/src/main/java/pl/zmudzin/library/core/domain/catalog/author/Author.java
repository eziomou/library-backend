package pl.zmudzin.library.core.domain.catalog.author;

import pl.zmudzin.library.core.domain.common.AbstractEntity;

import java.util.Objects;

public class Author extends AbstractEntity<AuthorId> {

    private final String firstName;
    private final String lastName;

    public Author(AuthorId id, String firstName, String lastName) {
        super(id);
        this.firstName = Objects.requireNonNull(firstName);
        this.lastName = Objects.requireNonNull(lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public Author withFirstName(String firstName) {
        return new Author(getId(), firstName, lastName);
    }

    public String getLastName() {
        return lastName;
    }

    public Author withLastName(String lastName) {
        return new Author(getId(), firstName, lastName);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
