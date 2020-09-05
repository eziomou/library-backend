package pl.zmudzin.library.spring.account;

import pl.zmudzin.library.core.domain.account.PasswordEncoder;

public class PasswordEncoderAdapter implements PasswordEncoder {

    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public PasswordEncoderAdapter(org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
