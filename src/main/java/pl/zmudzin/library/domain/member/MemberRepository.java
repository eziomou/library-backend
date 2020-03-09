package pl.zmudzin.library.domain.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.zmudzin.ddd.annotations.domain.DomainRepository;

import java.util.Optional;

/**
 * @author Piotr Żmudzin
 */
@DomainRepository
public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByAccountUsername(String username);

    boolean existsByAccountId(Long id);

    Page<Member> findAll(Specification<Member> specification, Pageable pageable);

    void delete(Member member);
}
