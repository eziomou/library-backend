package pl.zmudzin.library.application.account;

import pl.zmudzin.ddd.annotations.application.ApplicationService;

@ApplicationService
public interface AccountService {

    AccountData getAccountByUsername(String username);

    AccountData updateAccountByUsername(String username, AccountUpdateRequest request);
}
