package pl.zmudzin.library.application.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;

/**
 * @author Piotr Żmudzin
 */
public abstract class PersonPredicateBuilder<X> extends PredicateBuilder {

    protected PersonPredicateBuilder(CriteriaBuilder criteriaBuilder) {
        super(criteriaBuilder);
    }

    public PersonPredicateBuilder<X> firstName(String firstName) {
        like(getPath().get("firstName"), firstName);
        return this;
    }

    public PersonPredicateBuilder<X> lastName(String lastName) {
        like(getPath().get("lastName"), lastName);
        return this;
    }

    public PersonPredicateBuilder<X> fullName(String fullName) {
        like(getFullName(getPath(), criteriaBuilder), fullName);
        return this;
    }

    protected abstract Path<X> getPath();

    public static Expression<String> getFullName(Path<?> path, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.concat(criteriaBuilder.concat(path.get("firstName"), " "), path.get("lastName"));
    }

    public static <X> PersonPredicateBuilder<X> builder(Path<X> path, CriteriaBuilder criteriaBuilder) {
        return new PersonPredicateBuilder<>(criteriaBuilder) {

            @Override
            protected Path<X> getPath() {
                return path;
            }
        };
    }
}
