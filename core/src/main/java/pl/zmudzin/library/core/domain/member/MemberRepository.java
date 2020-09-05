package pl.zmudzin.library.core.domain.member;

import pl.zmudzin.library.core.domain.account.AccountId;
import pl.zmudzin.library.core.domain.common.Repository;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, MemberId> {

    Optional<Member> findByUsername(String username);

    boolean existsByAccountId(AccountId accountId);
}
