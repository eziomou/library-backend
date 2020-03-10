package pl.zmudzin.library.ui.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.application.member.*;
import pl.zmudzin.library.ui.util.ModelProcessorUtil;

import javax.validation.Valid;

/**
 * @author Piotr Żmudzin
 */
@RestController
@RequestMapping(path = "/members", produces = "application/json")
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EntityModel<MemberData>> createMember(@Valid @RequestBody MemberCreateRequest request) {
        MemberData data = memberService.createMember(request);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping("/{username}")
    public ResponseEntity<EntityModel<MemberData>> getMemberByUsername(@PathVariable String username) {
        MemberData data = memberService.getMemberByUsername(username);
        return ModelProcessorUtil.toResponseEntity(data);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<MemberData>>> getAllMembers(@Valid MemberSearchRequest request,
                                                                 @PageableDefault Pageable pageable) {
        Page<MemberData> data = memberService.getAllMembers(request, pageable);
        return ModelProcessorUtil.toResponseEntity(data);
    }
}
