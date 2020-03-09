package pl.zmudzin.library.util;

import org.mockito.Mockito;
import pl.zmudzin.library.application.member.MemberCreateRequest;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.member.MemberRepository;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 * @author Piotr Żmudzin
 */
public class MemberTestUtil {

    public static final Long ID = 1L;

    public static Member getMember(MemberRepository memberRepository, Account account) {
        return memberRepository.findByAccountUsername(account.getUsername()).orElseGet(() -> {
            Member member = new Member(account);
            member = memberRepository.save(member);
            return member;
        });
    }

    public static MemberCreateRequest getMemberCreateRequest() {
        return getMemberCreateRequest(AccountTestUtil.USERNAME);
    }

    public static MemberCreateRequest getMemberCreateRequest(String username) {
        MemberCreateRequest request = new MemberCreateRequest();
        request.setUsername(username);
        request.setPassword(AccountTestUtil.PASSWORD);
        request.setFirstName(AccountTestUtil.FIRST_NAME);
        request.setLastName(AccountTestUtil.LAST_NAME);
        return request;
    }

    public static Member mockMember() {
        return mockMember(ID, AccountTestUtil.mockAccount());
    }

    public static Member mockMember(Long id, Account account) {
        Member member = Mockito.mock(Member.class, withSettings().lenient());
        when(member.getId()).thenReturn(id);
        when(member.getAccount()).thenReturn(account);
        return member;
    }
}
