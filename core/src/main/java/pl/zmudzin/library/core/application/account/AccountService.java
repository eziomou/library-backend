package pl.zmudzin.library.core.application.account;

public interface AccountService {

    void updatePassword(String accountId, String password);

    void updateProfile(String accountId, String firstName, String lastName);

    AccountData getAccountById(String accountId);
}
