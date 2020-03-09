package pl.zmudzin.library.application.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Arrays;

/**
 * @author Piotr Żmudzin
 */
public class PredicateBuilder {

    protected CriteriaBuilder criteriaBuilder;
    protected Predicate predicate;

    protected PredicateBuilder(CriteriaBuilder criteriaBuilder) {
        this.criteriaBuilder = criteriaBuilder;
    }

    public PredicateBuilder addExpression(Expression<Boolean> expression) {
        if (expression == null) {
            return this;
        }
        if (predicate == null) {
            predicate = criteriaBuilder.conjunction();
        }
        predicate.getExpressions().add(expression);
        return this;
    }

    public PredicateBuilder equal(Path<?> path, Object object) {
        if (object != null) {
            addExpression(criteriaBuilder.equal(path, object));
        }
        return this;
    }

    public PredicateBuilder equal(Expression<?> expression, Object object) {
        if (object != null) {
            addExpression(criteriaBuilder.equal(expression, object));
        }
        return this;
    }

    public PredicateBuilder equal(Path<?> path, Object[] object) {
        if (object != null && object.length != 0) {
            addExpression(
                    criteriaBuilder.or(
                            Arrays.stream(object)
                                    .map(o -> criteriaBuilder.equal(path, o))
                                    .toArray(Predicate[]::new)
                    )
            );
        }
        return this;
    }

    public PredicateBuilder like(Expression<String> expression, String string) {
        if (string != null) {
            addExpression(like(criteriaBuilder, expression, string));
        }
        return this;
    }

    private static Predicate like(CriteriaBuilder cb, Expression<String> e, String s) {
        return cb.like(cb.lower(e), '%' + s.toLowerCase() + '%');
    }

    public Predicate build() {
        return predicate;
    }

    public static PredicateBuilder builder(CriteriaBuilder criteriaBuilder) {
        return new PredicateBuilder(criteriaBuilder);
    }
}
