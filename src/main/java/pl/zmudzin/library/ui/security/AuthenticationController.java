package pl.zmudzin.library.ui.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zmudzin.library.application.security.AuthenticationService;

import javax.validation.Valid;

/**
 * @author Piotr Żmudzin
 */
@RestController
@RequestMapping(path = "/auth", produces = "application/json")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        Object result = authenticationService.authenticate(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(result);
    }
}
