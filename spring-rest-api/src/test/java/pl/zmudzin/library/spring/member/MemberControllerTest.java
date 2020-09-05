package pl.zmudzin.library.spring.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.member.MemberData;
import pl.zmudzin.library.core.application.member.MemberQuery;
import pl.zmudzin.library.core.application.member.MemberService;
import pl.zmudzin.library.core.domain.account.Account;
import pl.zmudzin.library.core.domain.account.AccountId;
import pl.zmudzin.library.core.domain.account.Profile;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.member.Member;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.Role;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    private static final Account ACCOUNT = new Account(AccountId.of("1"), "Foo", "Bar", new Profile("Baz", "Qux"));

    private static final Member MEMBER = new Member(MemberId.of("1"), ACCOUNT);

    private static final MemberData MEMBER_DATA = MemberData.builder()
            .id(MEMBER.getId().toString())
            .account(builder -> builder
                    .username(ACCOUNT.getUsername())
                    .firstName(ACCOUNT.getProfile().getFirstName())
                    .lastName(ACCOUNT.getProfile().getLastName()))
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    void registerMember_validMember_returnsOk() throws Exception {
        RegisterMemberRequest request = new RegisterMemberRequest("Foo", "Baz", "Bar", "Qux");

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getMemberById_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/members/" + MEMBER.getId()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getMemberById_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(get("/members/" + MEMBER.getId()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getMemberById_asLibrarian_returnsOk() throws Exception {
        when(memberService.getMemberById(MEMBER.getId().toString())).thenReturn(MEMBER_DATA);

        mockMvc.perform(get("/members/" + MEMBER.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account.username", is(MEMBER_DATA.getAccount().getUsername())))
                .andExpect(jsonPath("$.account.profile.firstName", is(MEMBER_DATA.getAccount().getProfile().getFirstName())))
                .andExpect(jsonPath("$.account.profile.lastName", is(MEMBER_DATA.getAccount().getProfile().getLastName())))
                .andExpect(jsonPath("$.account.profile.fullName", is(MEMBER_DATA.getAccount().getProfile().getFullName())));
    }

    @Test
    void getAllMembers_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/members"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getAllMembers_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(get("/members"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getAllMembers_asLibrarian_returnsOk() throws Exception {
        MemberQuery query = new RestMemberQuery();
        Pagination pagination = new RestPagination();

        when(memberService.getAllMembersByQuery(query, pagination)).thenReturn(Paginated.of(MEMBER_DATA));

        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].account.username", is(MEMBER_DATA.getAccount().getUsername())))
                .andExpect(jsonPath("$.elements[0].account.profile.firstName", is(MEMBER_DATA.getAccount().getProfile().getFirstName())))
                .andExpect(jsonPath("$.elements[0].account.profile.lastName", is(MEMBER_DATA.getAccount().getProfile().getLastName())))
                .andExpect(jsonPath("$.elements[0].account.profile.fullName", is(MEMBER_DATA.getAccount().getProfile().getFullName())));
    }
}