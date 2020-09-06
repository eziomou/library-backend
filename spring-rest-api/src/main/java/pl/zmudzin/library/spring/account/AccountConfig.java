package pl.zmudzin.library.spring.account;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.zmudzin.library.core.application.account.AccountService;
import pl.zmudzin.library.core.application.account.AccountServiceImpl;
import pl.zmudzin.library.core.domain.account.AccountFactory;
import pl.zmudzin.library.core.domain.account.AccountRepository;
import pl.zmudzin.library.core.domain.account.PasswordEncoder;
import pl.zmudzin.library.persistence.jooq.account.JooqAccountRepository;

@Configuration
class AccountConfig {

    @Bean
    AccountRepository accountRepository(DSLContext context) {
        return new JooqAccountRepository(context);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new PasswordEncoderAdapter(new BCryptPasswordEncoder());
    }

    @Bean
    AccountFactory accountFactory(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        return new AccountFactory(accountRepository, passwordEncoder);
    }

    @Bean
    AccountService accountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        return new AccountServiceImpl(accountRepository, passwordEncoder);
    }
}
