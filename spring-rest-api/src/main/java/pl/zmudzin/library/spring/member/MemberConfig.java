package pl.zmudzin.library.spring.member;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.zmudzin.library.core.application.member.MemberReadonlyRepository;
import pl.zmudzin.library.core.application.member.MemberService;
import pl.zmudzin.library.core.application.member.MemberServiceImpl;
import pl.zmudzin.library.core.domain.account.AccountFactory;
import pl.zmudzin.library.core.domain.account.AccountRepository;
import pl.zmudzin.library.core.domain.member.MemberRepository;
import pl.zmudzin.library.persistence.jooq.member.JooqMemberRepository;

@Configuration
class MemberConfig {

    @Bean
    MemberRepository memberRepository(DSLContext context) {
        return new JooqMemberRepository(context);
    }

    @Bean
    MemberReadonlyRepository memberReadonlyRepository(DSLContext context) {
        return new JooqMemberRepository(context);
    }

    @Bean
    MemberService memberService(AccountFactory accountFactory, AccountRepository accountRepository,
                                MemberRepository memberRepository, MemberReadonlyRepository memberReadonlyRepository) {
        return new MemberServiceImpl(accountFactory, accountRepository, memberRepository, memberReadonlyRepository);
    }
}
