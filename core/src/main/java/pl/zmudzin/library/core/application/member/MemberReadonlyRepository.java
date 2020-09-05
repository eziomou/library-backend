package pl.zmudzin.library.core.application.member;

import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.common.Pagination;

import java.util.Optional;

public interface MemberReadonlyRepository {

    Optional<MemberData> findById(String memberId);

    Paginated<MemberData> findAllByQuery(MemberQuery query, Pagination pagination);
}
