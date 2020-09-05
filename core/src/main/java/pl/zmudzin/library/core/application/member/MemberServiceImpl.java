package pl.zmudzin.library.core.application.member;

import pl.zmudzin.library.core.application.common.NotFoundException;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.account.Account;
import pl.zmudzin.library.core.domain.account.AccountFactory;
import pl.zmudzin.library.core.domain.account.AccountRepository;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.member.Member;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.member.MemberRepository;

import java.util.UUID;

public class MemberServiceImpl implements MemberService {

    private final AccountFactory accountFactory;
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final MemberReadonlyRepository memberReadonlyRepository;

    public MemberServiceImpl(AccountFactory accountFactory, AccountRepository accountRepository,
                             MemberRepository memberRepository, MemberReadonlyRepository memberReadonlyRepository) {
        this.accountFactory = accountFactory;
        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
        this.memberReadonlyRepository = memberReadonlyRepository;
    }

    @Override
    public void registerMember(RegisterMemberCommand command) {
        Account account = accountFactory.createAccount(command.getUsername(), command.getPassword(),
                command.getFirstName(), command.getLastName());
        accountRepository.save(account);

        Member member = new Member(new MemberId(UUID.randomUUID().toString()), account);
        memberRepository.save(member);
    }

    @Override
    public MemberData getMemberById(String memberId) {
        return memberReadonlyRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found: " + memberId));
    }

    @Override
    public Paginated<MemberData> getAllMembersByQuery(MemberQuery query, Pagination pagination) {
        return memberReadonlyRepository.findAllByQuery(query, pagination);
    }
}
