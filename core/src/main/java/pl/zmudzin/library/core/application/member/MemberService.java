package pl.zmudzin.library.core.application.member;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;

public interface MemberService {

    void registerMember(RegisterMemberCommand command);

    MemberData getMemberById(String memberId);

    Paginated<MemberData> getAllMembersByQuery(MemberQuery query, Pagination pagination);
}
