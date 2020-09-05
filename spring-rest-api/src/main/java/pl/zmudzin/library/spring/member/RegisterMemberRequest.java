package pl.zmudzin.library.spring.member;

import pl.zmudzin.library.spring.account.CreateAccountRequest;

public class RegisterMemberRequest extends CreateAccountRequest {

    public RegisterMemberRequest() {
    }

    public RegisterMemberRequest(String foo, String baz, String bar, String qux) {
        super(foo, baz, bar, qux);
    }
}
