package pl.zmudzin.library.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.zmudzin.ddd.annotations.domain.DomainRepositoryImpl;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.member.MemberRepository;

import java.util.Optional;

/**
 * @author Piotr Żmudzin
 */
@DomainRepositoryImpl
public interface MemberJpaRepository extends MemberRepository, JpaRepository<Member, Long>,
        JpaSpecificationExecutor<Member> {

    @EntityGraph(value = "member-entity-graph")
    Optional<Member> findByAccountUsername(String username);

    @EntityGraph(value = "member-entity-graph")
    Page<Member> findAll(Specification<Member> specification, Pageable pageable);
}
