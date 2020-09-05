package pl.zmudzin.library.persistence.jooq;

import org.jooq.*;
import pl.zmudzin.library.core.domain.common.Entity;
import pl.zmudzin.library.core.domain.common.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractJooqRepository<E extends Entity<ID>, ID> implements Repository<E, ID> {

    protected final DSLContext context;
    protected final Table<?> table;

    protected AbstractJooqRepository(DSLContext context, Table<?> table) {
        this.context = context;
        this.table = table;
    }

    protected abstract Map<Field<?>, Object> extractFields(E entity);

    protected abstract E mapToDomainModel(Record record);

    protected abstract Condition eq(ID id);

    protected SelectFromStep<? extends Record> selectStep() {
        return selectStep(DSLContext::select);
    }

    protected SelectFromStep<? extends Record> selectStep(Function<DSLContext, SelectFromStep<? extends Record>> function) {
        return function.apply(context);
    }

    protected SelectJoinStep<? extends Record> fromStep() {
        return fromStep(selectStep());
    }

    protected SelectJoinStep<? extends Record> fromStep(SelectFromStep<? extends Record> fromStep) {
        return fromStep.from(table);
    }

    protected SelectJoinStep<? extends Record> joinStep() {
        return joinStep(fromStep());
    }

    protected SelectJoinStep<? extends Record> joinStep(SelectFromStep<? extends Record> fromStep) {
        return joinStep((SelectJoinStep<? extends Record>) fromStep);
    }

    protected SelectJoinStep<? extends Record> joinStep(SelectJoinStep<? extends Record> joinStep) {
        return joinStep;
    }

    @Override
    public int size() {
        return context.fetchCount(table);
    }

    protected int count(Condition... conditions) {
        return joinStep(fromStep(selectStep(DSLContext::selectCount))).where(conditions).fetchOne(0, Integer.class);
    }

    @Override
    public void save(E entity) {
        context.insertInto(table).values(extractFields(entity).values()).execute();
    }

    @Override
    public void saveAll(Collection<E> entities) {
        context.batch(createQueries(entities)).execute();
    }

    private Collection<? extends Query> createQueries(Collection<E> entities) {
        return entities.stream().map(e -> context.insertInto(table).values(extractFields(e).values())).collect(Collectors.toList());
    }

    @Override
    public boolean exists(ID id) {
        return context.fetchExists(context.selectOne().from(table).where(eq(id)));
    }

    @Override
    public Optional<E> find(ID id) {
        return find(eq(id));
    }

    protected Optional<E> find(Condition... conditions) {
        return Optional.ofNullable(joinStep().where(conditions).fetchOne(this::mapToDomainModel));
    }

    @Override
    public void update(E entity) {
        update(entity, eq(entity.getId()));
    }

    protected void update(E entity, Condition... conditions) {
        context.update(table).set(extractFields(entity)).where(conditions).execute();
    }

    @Override
    public void delete(E entity) {
        delete(eq(entity.getId()));
    }

    protected void delete(Condition... conditions) {
        context.delete(table).where(conditions).execute();
    }

    @Override
    public void deleteAll() {
        context.delete(table).execute();
    }
}
