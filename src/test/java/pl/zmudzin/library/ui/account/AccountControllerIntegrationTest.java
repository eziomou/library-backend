package pl.zmudzin.library.ui.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import pl.zmudzin.library.application.account.AccountUpdateRequest;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.account.Account;
import pl.zmudzin.library.domain.account.AccountRepository;
import pl.zmudzin.library.util.AccountTestUtil;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr Żmudzin
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void getAccountByUsername_notAuthenticated_401() throws Exception {
        mockMvc.perform(get("/accounts/" + AccountTestUtil.USERNAME))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void getAccountByUsername_notAuthorized_403() throws Exception {
        mockMvc.perform(get("/accounts/" + AccountTestUtil.USERNAME))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = AccountTestUtil.USERNAME, roles = "MEMBER")
    @Test
    void getAccountByUsername_asOwner_200() throws Exception {
        Account account = AccountTestUtil.getAccount(accountRepository);

        ResultActions resultActions = mockMvc.perform(get("/accounts/" + AccountTestUtil.USERNAME));

        validate(account, resultActions);
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void getAccountByUsername_asLibrarian_200() throws Exception {
        Account account = AccountTestUtil.getAccount(accountRepository);

        ResultActions resultActions = mockMvc.perform(get("/accounts/" + AccountTestUtil.USERNAME));

        validate(account, resultActions);
    }

    @Test
    void updateAccountByUsername_notAuthenticated_401() throws Exception {
        AccountUpdateRequest request = new AccountUpdateRequest();

        mockMvc.perform(put("/accounts/" + AccountTestUtil.USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void updateAccountByUsername_notAuthorized_403() throws Exception {
        AccountUpdateRequest request = new AccountUpdateRequest();

        mockMvc.perform(put("/accounts/" + AccountTestUtil.USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = AccountTestUtil.USERNAME, roles = "MEMBER")
    @Test
    void updateAccountByUsername_asOwner_200() throws Exception {
        Account account = AccountTestUtil.getAccount(accountRepository);
        AccountUpdateRequest request = getAccountUpdateRequest(account);

        ResultActions resultActions = mockMvc.perform(put("/accounts/" + AccountTestUtil.USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        validate(request, resultActions);
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void updateAccountByUsername_asLibrarian_200() throws Exception {
        Account account = AccountTestUtil.getAccount(accountRepository);
        AccountUpdateRequest request = getAccountUpdateRequest(account);

        ResultActions resultActions = mockMvc.perform(put("/accounts/" + AccountTestUtil.USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        validate(request, resultActions);
    }

    private AccountUpdateRequest getAccountUpdateRequest(Account account) {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setFirstName(account.getProfile().getFirstName() + "Updated");
        request.setLastName(account.getProfile().getLastName() + "Updated");
        return request;
    }

    private static void validate(Account account, ResultActions resultActions) throws Exception {
        validate(account.getProfile().getFirstName(), account.getProfile().getLastName(), resultActions);
    }

    private static void validate(AccountUpdateRequest request, ResultActions resultActions) throws Exception {
        validate(request.getFirstName(), request.getLastName(), resultActions);
    }

    private static void validate(String firstName, String lastName, ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(firstName)))
                .andExpect(jsonPath("$.lastName", is(lastName)));
    }
}