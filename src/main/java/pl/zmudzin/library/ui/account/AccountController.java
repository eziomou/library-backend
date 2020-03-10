package pl.zmudzin.library.ui.account;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.application.account.AccountData;
import pl.zmudzin.library.application.account.AccountService;
import pl.zmudzin.library.application.account.AccountUpdateRequest;
import pl.zmudzin.library.ui.util.ModelProcessorUtil;

import javax.validation.Valid;

/**
 * @author Piotr Żmudzin
 */
@RestController
@RequestMapping(path = "/accounts", produces = "application/json")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<EntityModel<AccountData>> getAccountByUsername(@PathVariable String username) {
        AccountData data = accountService.getAccountByUsername(username);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @PutMapping(path = "/{username}", consumes = "application/json")
    public ResponseEntity<EntityModel<AccountData>> updateAccountById(@PathVariable String username,
                                                                     @Valid @RequestBody AccountUpdateRequest request) {
        AccountData data = accountService.updateAccountByUsername(username, request);
        return ModelProcessorUtil.toResponseEntity(data);
    }
}
