package pl.zmudzin.library.spring.account;

import javax.validation.constraints.NotNull;

public class UpdatePasswordRequest {

    @NotNull
    private String password;

    public UpdatePasswordRequest() {
    }

    public UpdatePasswordRequest(@NotNull String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
