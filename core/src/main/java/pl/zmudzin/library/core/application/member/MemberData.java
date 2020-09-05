package pl.zmudzin.library.core.application.member;

import pl.zmudzin.library.core.application.account.AccountData;

import java.util.function.Consumer;

public final class MemberData {

    private final String id;
    private final AccountData account;

    private MemberData(Builder builder) {
        this.id = builder.id;
        this.account = builder.account;
    }

    public String getId() {
        return id;
    }

    public AccountData getAccount() {
        return account;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private AccountData account;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder account(Consumer<AccountData.Builder> consumer) {
            AccountData.Builder builder = AccountData.builder();
            consumer.accept(builder);
            this.account = builder.build();
            return this;
        }

        public MemberData build() {
            return new MemberData(this);
        }
    }
}
