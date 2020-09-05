package pl.zmudzin.library.core.domain.account;

public interface PasswordEncoder {

    String encode(String password);
}
