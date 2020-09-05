package pl.zmudzin.library.persistence.jooq.librarian;

import org.jooq.*;
import pl.zmudzin.library.core.domain.account.Account;
import pl.zmudzin.library.core.domain.account.AccountId;
import pl.zmudzin.library.core.domain.account.Profile;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.librarian.Librarian;
import pl.zmudzin.library.core.domain.librarian.LibrarianId;
import pl.zmudzin.library.core.domain.librarian.LibrarianRepository;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepository;
import pl.zmudzin.library.persistence.jooq.schema.Tables;

import java.util.*;

public class JooqLibrarianRepository extends AbstractJooqRepository<Librarian, LibrarianId> implements LibrarianRepository {

    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Librarian LIBRARIAN = Tables.LIBRARIAN;
    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Account ACCOUNT = Tables.ACCOUNT;

    public JooqLibrarianRepository(DSLContext context) {
        super(context, LIBRARIAN);
    }

    @Override
    protected Map<Field<?>, Object> extractFields(Librarian member) {
        Map<Field<?>, Object> map = new LinkedHashMap<>();
        map.put(LIBRARIAN.ID, member.getId().toString());
        map.put(LIBRARIAN.ACCOUNT_ID, member.getAccount().getId().toString());
        return map;
    }

    @Override
    protected Librarian mapToDomainModel(Record record) {
        return new Librarian(
                LibrarianId.of(record.get(LIBRARIAN.ID).toString()),
                new Account(
                        AccountId.of(record.get(ACCOUNT.ID).toString()),
                        record.get(ACCOUNT.USERNAME), record.get(ACCOUNT.PASSWORD),
                        new Profile(record.get(ACCOUNT.FIRST_NAME), record.get(ACCOUNT.LAST_NAME))
                )
        );
    }

    @Override
    protected Condition eq(LibrarianId librarianId) {
        return LIBRARIAN.ID.eq(UUID.fromString(librarianId.toString()));
    }

    @Override
    protected SelectJoinStep<? extends Record> joinStep() {
        return super.joinStep().innerJoin(ACCOUNT).on(LIBRARIAN.ACCOUNT_ID.eq(ACCOUNT.ID));
    }

    @Override
    public Optional<Librarian> findByUsername(String username) {
        return find(ACCOUNT.USERNAME.eq(username));
    }

    @Override
    public boolean existsByAccountId(AccountId accountId) {
        return context.fetchExists(context.selectOne()
                .from(LIBRARIAN)
                .where(LIBRARIAN.ACCOUNT_ID.eq(UUID.fromString(accountId.toString()))));
    }

    @Override
    public List<Librarian> findAll(Pagination pagination) {
        return joinStep()
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .fetch(this::mapToDomainModel);
    }
}
