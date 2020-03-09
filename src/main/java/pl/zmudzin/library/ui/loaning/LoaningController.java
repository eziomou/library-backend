package pl.zmudzin.library.ui.loaning;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.application.loaning.*;
import pl.zmudzin.library.util.ModelProcessorUtil;

import javax.validation.Valid;

/**
 * @author Piotr Żmudzin
 */
@RestController
@RequestMapping(path = "/loans", produces = "application/json")
public class LoaningController {

    private LoanService loanService;

    public LoaningController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EntityModel<LoanData>> createLoan(@Valid @RequestBody LoanCreateRequest request) {
        LoanData data = loanService.createLoan(request);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<LoanData>> getLoanById(@PathVariable Long id) {
        LoanData data = loanService.getLoanById(id);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<LoanData>>> getAllLoans(@Valid LoanSearchRequest request,
                                                                           @PageableDefault Pageable pageable) {
        Page<LoanData> data = loanService.getAllLoans(request, pageable);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<EntityModel<LoanData>> updateLoanById(@PathVariable Long id,
                                                                  @Valid @RequestBody LoanUpdateRequest request) {
        LoanData data = loanService.updateLoanById(id, request);
        return ModelProcessorUtil.toResponseEntity(data);
    }
}
