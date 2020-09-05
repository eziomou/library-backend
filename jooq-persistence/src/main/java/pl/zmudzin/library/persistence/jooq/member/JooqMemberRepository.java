package pl.zmudzin.library.persistence.jooq.member;

import org.jooq.*;
import pl.zmudzin.library.core.application.common.Metadata;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.member.MemberData;
import pl.zmudzin.library.core.application.member.MemberQuery;
import pl.zmudzin.library.core.application.member.MemberReadonlyRepository;
import pl.zmudzin.library.core.domain.account.Account;
import pl.zmudzin.library.core.domain.account.AccountId;
import pl.zmudzin.library.core.domain.account.Profile;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.member.Member;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.member.MemberRepository;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepository;
import pl.zmudzin.library.persistence.jooq.schema.Tables;

import java.util.*;

public class JooqMemberRepository extends AbstractJooqRepository<Member, MemberId> implements MemberRepository, MemberReadonlyRepository {

    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Member MEMBER = Tables.MEMBER;
    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Account ACCOUNT = Tables.ACCOUNT;

    public JooqMemberRepository(DSLContext context) {
        super(context, MEMBER);
    }

    @Override
    protected Map<Field<?>, Object> extractFields(Member member) {
        Map<Field<?>, Object> map = new LinkedHashMap<>();
        map.put(MEMBER.ID, member.getId().toString());
        map.put(MEMBER.ACCOUNT_ID, member.getAccount().getId().toString());
        return map;
    }

    @Override
    protected Member mapToDomainModel(Record record) {
        return new Member(
                MemberId.of(record.getValue(MEMBER.ID).toString()),
                new Account(
                        AccountId.of(record.getValue(ACCOUNT.ID).toString()),
                        record.getValue(ACCOUNT.USERNAME), record.getValue(ACCOUNT.PASSWORD),
                        new Profile(record.getValue(ACCOUNT.FIRST_NAME), record.getValue(ACCOUNT.LAST_NAME))
                )
        );
    }

    protected MemberData mapToViewModel(Record record) {
        return MemberData.builder()
                .id(record.getValue(MEMBER.ID).toString())
                .account(builder -> builder
                        .username(record.getValue(ACCOUNT.USERNAME))
                        .firstName(record.getValue(ACCOUNT.FIRST_NAME))
                        .lastName(record.getValue(ACCOUNT.LAST_NAME)))
                .build();
    }

    @Override
    protected Condition eq(MemberId memberId) {
        return MEMBER.ID.eq(UUID.fromString(memberId.toString()));
    }

    @Override
    protected SelectJoinStep<? extends Record> joinStep() {
        return super.joinStep().innerJoin(ACCOUNT).on(MEMBER.ACCOUNT_ID.eq(ACCOUNT.ID));
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return find(ACCOUNT.USERNAME.eq(username));
    }

    @Override
    public boolean existsByAccountId(AccountId accountId) {
        return context.fetchExists(context.selectOne()
                .from(MEMBER)
                .where(MEMBER.ACCOUNT_ID.eq(UUID.fromString(accountId.toString()))));
    }

    @Override
    public Optional<MemberData> findById(String memberId) {
        return Optional.ofNullable(joinStep().where(eq(MemberId.of(memberId))).fetchOne(this::mapToViewModel));
    }

    @Override
    public Paginated<MemberData> findAllByQuery(MemberQuery query, Pagination pagination) {
        List<Condition> conditions = createConditions(query);
        int total = count(conditions.toArray(Condition[]::new));
        return Paginated.of(joinStep()
                .where(conditions)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .fetch(this::mapToViewModel), Metadata.of(pagination, total));
    }

    private List<Condition> createConditions(MemberQuery query) {
        List<Condition> conditions = new ArrayList<>();
        query.firstName().ifPresent(firstName -> conditions.add(ACCOUNT.FIRST_NAME.eq(firstName)));
        query.lastName().ifPresent(lastName -> conditions.add(ACCOUNT.LAST_NAME.eq(lastName)));
        return conditions;
    }
}
