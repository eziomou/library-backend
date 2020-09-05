package pl.zmudzin.library.spring.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.zmudzin.library.core.application.account.AccountData;
import pl.zmudzin.library.core.application.account.AccountService;
import pl.zmudzin.library.core.domain.account.Account;
import pl.zmudzin.library.core.domain.account.AccountId;
import pl.zmudzin.library.core.domain.account.Profile;
import pl.zmudzin.library.spring.security.AuthorizationService;
import pl.zmudzin.library.spring.security.Role;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest {

    private static final Account ACCOUNT = new Account(AccountId.of("1"), "Foo", "Bar", new Profile("Baz", "Qux"));

    private static final AccountData ACCOUNT_DATA = AccountData.builder()
            .username(ACCOUNT.getUsername())
            .firstName(ACCOUNT.getProfile().getFirstName())
            .lastName(ACCOUNT.getProfile().getLastName())
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AuthorizationService authorizationService;

    @Test
    void getAccountById_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/account"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void getAccountById_asMember_returnsOk() throws Exception {
        when(accountService.getAccountById(ACCOUNT.getId().toString())).thenReturn(ACCOUNT_DATA);

        when(authorizationService.getAccountId()).thenReturn(ACCOUNT.getId().toString());

        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(ACCOUNT_DATA.getUsername())))
                .andExpect(jsonPath("$.profile.firstName", is(ACCOUNT_DATA.getProfile().getFirstName())))
                .andExpect(jsonPath("$.profile.lastName", is(ACCOUNT_DATA.getProfile().getLastName())))
                .andExpect(jsonPath("$.profile.fullName", is(ACCOUNT_DATA.getProfile().getFullName())));
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void getAccountById_asLibrarian_returnsOk() throws Exception {
        when(accountService.getAccountById(ACCOUNT.getId().toString())).thenReturn(ACCOUNT_DATA);

        when(authorizationService.getAccountId()).thenReturn(ACCOUNT.getId().toString());

        mockMvc.perform(get("/account"))
                .andExpect(status().isOk());
    }

    @Test
    void updatePassword_unauthenticated_returnsUnauthorized() throws Exception {
        UpdatePasswordRequest request = new UpdatePasswordRequest("Secret");

        mockMvc.perform(put("/account/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void updatePassword_asMember_returnsOk() throws Exception {
        UpdatePasswordRequest request = new UpdatePasswordRequest("Secret");

        when(accountService.getAccountById(ACCOUNT.getId().toString())).thenReturn(ACCOUNT_DATA);

        when(authorizationService.getAccountId()).thenReturn(ACCOUNT.getId().toString());

        mockMvc.perform(put("/account/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void updatePassword_asLibrarian_returnsOk() throws Exception {
        UpdatePasswordRequest request = new UpdatePasswordRequest("Secret");

        when(accountService.getAccountById(ACCOUNT.getId().toString())).thenReturn(ACCOUNT_DATA);

        when(authorizationService.getAccountId()).thenReturn(ACCOUNT.getId().toString());

        mockMvc.perform(put("/account/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateProfile_unauthenticated_returnsUnauthorized() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest("Qux", "Baz");

        mockMvc.perform(put("/account/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void updateProfile_asMember_returnsOk() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest("Qux", "Baz");

        when(accountService.getAccountById(ACCOUNT.getId().toString())).thenReturn(ACCOUNT_DATA);

        when(authorizationService.getAccountId()).thenReturn(ACCOUNT.getId().toString());

        mockMvc.perform(put("/account/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void updateProfile_asLibrarian_returnsOk() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest("Qux", "Baz");

        when(accountService.getAccountById(ACCOUNT.getId().toString())).thenReturn(ACCOUNT_DATA);

        when(authorizationService.getAccountId()).thenReturn(ACCOUNT.getId().toString());

        mockMvc.perform(put("/account/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}