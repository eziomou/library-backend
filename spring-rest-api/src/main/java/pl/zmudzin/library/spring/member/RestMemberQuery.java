package pl.zmudzin.library.spring.member;

import pl.zmudzin.library.core.application.member.MemberQuery;

import java.util.Objects;
import java.util.Optional;

public class RestMemberQuery implements MemberQuery {

    private String firstName;
    private String lastName;

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
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof RestMemberQuery))
            return false;
        RestMemberQuery other = (RestMemberQuery) object;
        return Objects.equals(firstName, other.firstName) &&
                Objects.equals(lastName, other.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
