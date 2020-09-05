package pl.zmudzin.library.spring.account;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.core.application.account.AccountData;
import pl.zmudzin.library.core.application.account.AccountService;
import pl.zmudzin.library.spring.security.AuthorizationService;
import pl.zmudzin.library.spring.security.Role;

import javax.validation.Valid;

@RestController
@RequestMapping
public class AccountController {

    private final AccountService accountService;
    private final AuthorizationService authorizationService;

    public AccountController(AccountService accountService, AuthorizationService authorizationService) {
        this.accountService = accountService;
        this.authorizationService = authorizationService;
    }

    @Secured({Role.MEMBER, Role.LIBRARIAN})
    @GetMapping(path = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountData> getAccountById() {
        AccountData account = accountService.getAccountById(authorizationService.getAccountId());
        return ResponseEntity.ok(account);
    }

    @Secured({Role.MEMBER, Role.LIBRARIAN})
    @PutMapping(path = "/account/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        accountService.updatePassword(authorizationService.getAccountId(), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @Secured({Role.MEMBER, Role.LIBRARIAN})
    @PutMapping(path = "/account/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        accountService.updateProfile(authorizationService.getAccountId(), request.getFirstName(), request.getLastName());
        return ResponseEntity.ok().build();
    }
}
