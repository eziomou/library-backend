package pl.zmudzin.library.persistence.jooq.member;

import org.junit.jupiter.api.AfterEach;
import pl.zmudzin.library.core.domain.member.Member;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.member.MemberRepository;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepositoryTest;
import pl.zmudzin.library.persistence.jooq.PersistenceUtil;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JooqMemberRepositoryTest extends AbstractJooqRepositoryTest<MemberRepository, Member, MemberId> {

    private final PersistenceUtil persistenceUtil;

    public JooqMemberRepositoryTest() {
        super(new JooqMemberRepository(context));
        persistenceUtil = new PersistenceUtil(context);
    }

    @Override
    protected Member getEntity() {
        return new Member(MemberId.of(UUID.randomUUID().toString()), persistenceUtil.randomAccount());
    }

    @Override
    protected Member getUpdatedEntity(Member entity) {
        return entity;
    }

    @Override
    protected void assertEntityEquals(Member expected, Member result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getAccount().getId(), result.getAccount().getId());
    }

    @AfterEach
    @Override
    protected void afterEach() {
        super.afterEach();
        persistenceUtil.removeAll();
    }
}