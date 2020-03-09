package pl.zmudzin.library.application.member;

import pl.zmudzin.library.application.util.PersonPredicateBuilder;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.member.Member;

import javax.persistence.criteria.*;

/**
 * @author Piotr Żmudzin
 */
public class MemberPredicateBuilder extends PersonPredicateBuilder<Member> {

    private Root<Member> member;
    private Join<Member, Account> account;

    private MemberPredicateBuilder(Root<Member> member, CriteriaBuilder criteriaBuilder) {
        super(criteriaBuilder);
        this.member = member;
    }

    private Join<Member, Account> getAccount() {
        if (account == null) {
            account = member.join("account", JoinType.LEFT);
        }
        return account;
    }

    public MemberPredicateBuilder username(String username) {
        equal(getAccount().get("username"), username);
        return this;
    }

    @Override
    protected Path<Member> getPath() {
        return getAccount().get("profile");
    }

    public static MemberPredicateBuilder builder(Root<Member> member, CriteriaBuilder criteriaBuilder) {
        return new MemberPredicateBuilder(member, criteriaBuilder);
    }
}
