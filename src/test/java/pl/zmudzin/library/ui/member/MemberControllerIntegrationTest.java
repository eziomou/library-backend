package pl.zmudzin.library.ui.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.zmudzin.library.application.member.MemberCreateRequest;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.account.AccountRepository;
import pl.zmudzin.library.domain.member.Member;
import pl.zmudzin.library.domain.member.MemberRepository;
import pl.zmudzin.library.util.AccountTestUtil;
import pl.zmudzin.library.util.MemberTestUtil;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr Żmudzin
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MemberControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void createMember_nullUsername_400() throws Exception {
        MemberCreateRequest request = MemberTestUtil.getMemberCreateRequest();
        request.setUsername(null);

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMember_nullPassword_400() throws Exception {
        MemberCreateRequest request = MemberTestUtil.getMemberCreateRequest();
        request.setPassword(null);

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMember_nullFirstName_400() throws Exception {
        MemberCreateRequest request = MemberTestUtil.getMemberCreateRequest();
        request.setFirstName(null);

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMember_nullLastName_400() throws Exception {
        MemberCreateRequest request = MemberTestUtil.getMemberCreateRequest();
        request.setLastName(null);

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMember_memberAlreadyExists_400() throws Exception {
        getMember();

        MemberCreateRequest request = MemberTestUtil.getMemberCreateRequest();

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMember_validArguments_200() throws Exception {
        MemberCreateRequest request = MemberTestUtil.getMemberCreateRequest("rYFSgsQDca");

        ResultActions resultActions = mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        Member member = memberRepository.findByAccountUsername(request.getUsername()).get();

        validate(member, resultActions);
    }

    @Test
    void getMemberByUsername_notExistingMember_404() throws Exception {
        mockMvc.perform(get("/members/" + "xyz"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    void getMemberByUsername_notAuthorized_403() throws Exception {
        getMember();

        mockMvc.perform(get("/members/" + AccountTestUtil.USERNAME))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = AccountTestUtil.USERNAME, roles = Roles.WithoutPrefix.MEMBER)
    @Test
    void getMemberByUsername_asOwner_200() throws Exception {
        Member member = getMember();

        ResultActions resultActions = mockMvc.perform(get("/members/" + member.getAccount().getUsername()));

        validate(member, resultActions);
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void getMemberByUsername_asLibrarian_200() throws Exception {
        Member member = getMember();

        ResultActions resultActions = mockMvc.perform(get("/members/" + member.getAccount().getUsername()));

        validate(member, resultActions);
    }

    private Member getMember() {
        Account account = AccountTestUtil.getAccount(accountRepository);
        return MemberTestUtil.getMember(memberRepository, account);
    }

    private static void validate(Member member, ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(member.getAccount().getUsername())))
                .andExpect(jsonPath("$.firstName", is(member.getAccount().getProfile().getFirstName())))
                .andExpect(jsonPath("$.lastName", is(member.getAccount().getProfile().getLastName())));
    }
}