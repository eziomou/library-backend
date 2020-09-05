package pl.zmudzin.library.persistence.jooq.account;

import org.jooq.*;
import pl.zmudzin.library.core.domain.account.Account;
import pl.zmudzin.library.core.domain.account.AccountId;
import pl.zmudzin.library.core.domain.account.AccountRepository;
import pl.zmudzin.library.core.domain.account.Profile;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepository;
import pl.zmudzin.library.persistence.jooq.schema.Tables;

import java.util.*;

public class JooqAccountRepository extends AbstractJooqRepository<Account, AccountId> implements AccountRepository {

    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Account ACCOUNT = Tables.ACCOUNT;

    public JooqAccountRepository(DSLContext context) {
        super(context, ACCOUNT);
    }

    @Override
    protected Map<Field<?>, Object> extractFields(Account author) {
        Map<Field<?>, Object> map = new LinkedHashMap<>();
        map.put(ACCOUNT.ID, author.getId().toString());
        map.put(ACCOUNT.USERNAME, author.getUsername());
        map.put(ACCOUNT.PASSWORD, author.getPassword());
        map.put(ACCOUNT.FIRST_NAME, author.getProfile().getFirstName());
        map.put(ACCOUNT.LAST_NAME, author.getProfile().getLastName());
        return map;
    }

    @Override
    protected Account mapToDomainModel(Record record) {
        return new Account(
                AccountId.of(record.get(ACCOUNT.ID).toString()),
                record.get(ACCOUNT.USERNAME),
                record.get(ACCOUNT.PASSWORD),
                new Profile(
                        record.get(ACCOUNT.FIRST_NAME),
                        record.get(ACCOUNT.LAST_NAME)
                )
        );
    }

    @Override
    protected Condition eq(AccountId accountId) {
        return ACCOUNT.ID.eq(UUID.fromString(accountId.toString()));
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        return Optional.ofNullable(fromStep().where(ACCOUNT.USERNAME.eq(username)).fetchOne(this::mapToDomainModel));
    }

    @Override
    public boolean existsByUsername(String username) {
        return context.fetchExists(fromStep().where(ACCOUNT.USERNAME.eq(username)));
    }
}
