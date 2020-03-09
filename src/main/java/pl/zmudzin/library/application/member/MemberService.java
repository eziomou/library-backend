package pl.zmudzin.library.application.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.zmudzin.ddd.annotations.application.ApplicationService;

/**
 * @author Piotr Żmudzin
 */
@ApplicationService
public interface MemberService {

    MemberData createMember(MemberCreateRequest request);

    MemberData getMemberByUsername(String username);

    Page<MemberData> getAllMembers(MemberSearchRequest request, Pageable pageable);
}
