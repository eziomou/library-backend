package pl.zmudzin.library.spring.member;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.member.MemberData;
import pl.zmudzin.library.core.application.member.MemberService;
import pl.zmudzin.library.core.application.member.RegisterMemberCommand;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.Role;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerMember(@Valid @RequestBody RegisterMemberRequest request) {
        memberService.registerMember(RegisterMemberCommand.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build());
        return ResponseEntity.ok().build();
    }

    @Secured({Role.LIBRARIAN})
    @GetMapping(path = "/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberData> getMemberById(@PathVariable String memberId) {
        MemberData member = memberService.getMemberById(memberId);
        return ResponseEntity.ok(member);
    }

    @Secured({Role.LIBRARIAN})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Paginated<MemberData>> getAllMembers(@Valid RestMemberQuery query, @Valid RestPagination pagination) {
        Paginated<MemberData> members = memberService.getAllMembersByQuery(query, pagination);
        return ResponseEntity.ok(members);
    }
}
