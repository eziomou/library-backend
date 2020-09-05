package pl.zmudzin.library.core.application.member;

import java.util.Optional;

public interface MemberQuery {

    Optional<String> firstName();

    Optional<String> lastName();
}
