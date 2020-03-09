package pl.zmudzin.library.ui.catalog.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.zmudzin.library.application.catalog.publisher.PublisherCreateRequest;
import pl.zmudzin.library.application.catalog.publisher.PublisherUpdateRequest;
import pl.zmudzin.library.application.security.Roles;
import pl.zmudzin.library.domain.catalog.Publisher;
import pl.zmudzin.library.domain.catalog.PublisherRepository;
import pl.zmudzin.library.util.PublisherTestUtil;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr Żmudzin
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PublisherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void createPublisher_nullName_400() throws Exception {
        PublisherCreateRequest request = PublisherTestUtil.getPublisherCreateRequest();
        request.setName(null);

        mockMvc.perform(post("/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPublisher_notAuthenticated_401() throws Exception {
        PublisherCreateRequest request = PublisherTestUtil.getPublisherCreateRequest();

        mockMvc.perform(post("/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void createPublisher_notAuthorized_403() throws Exception {
        PublisherCreateRequest request = PublisherTestUtil.getPublisherCreateRequest();

        mockMvc.perform(post("/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void createPublisher_validArguments_200() throws Exception {
        PublisherCreateRequest request = PublisherTestUtil.getPublisherCreateRequest();

        ResultActions resultActions = mockMvc.perform(post("/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        validate(request, resultActions);
    }

    @Test
    void getPublisherById_nonExistingPublisher_404() throws Exception {
        mockMvc.perform(get("/publishers/" + Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPublisherById_existingPublisher_200() throws Exception {
        Publisher publisher = PublisherTestUtil.createPublisher(publisherRepository);

        ResultActions resultActions = mockMvc.perform(get("/publishers/" + publisher.getId()));

        validate(publisher, resultActions);
    }

    @Test
    void updatePublisher_notAuthenticated_401() throws Exception {
        PublisherUpdateRequest request = new PublisherUpdateRequest();

        mockMvc.perform(put("/publishers/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void updatePublisher_notAuthorized_403() throws Exception {
        PublisherUpdateRequest request = new PublisherUpdateRequest();

        mockMvc.perform(put("/publishers/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void updatePublisher_nonExistingPublisher_404() throws Exception {
        PublisherUpdateRequest request = new PublisherUpdateRequest();

        mockMvc.perform(put("/publishers/" + Long.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void updatePublisher_validArguments_200() throws Exception {
        Publisher publisher = PublisherTestUtil.createPublisher(publisherRepository);

        PublisherUpdateRequest request = new PublisherUpdateRequest();
        request.setName(publisher.getName() + "Updated");

        ResultActions resultActions = mockMvc.perform(put("/publishers/" + publisher.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        validate(request, resultActions);
    }

    @Test
    void deletePublisher_notAuthenticated_401() throws Exception {
        mockMvc.perform(delete("/publishers/" + 1000))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void deletePublisher_notAuthorized_403() throws Exception {
        mockMvc.perform(delete("/publishers/" + 1000))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void deletePublisher_notExistingPublisher_404() throws Exception {
        mockMvc.perform(delete("/publishers/" + 1000))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = Roles.WithoutPrefix.LIBRARIAN)
    @Test
    void deletePublisher_validArguments_200() throws Exception {
        Publisher publisher = PublisherTestUtil.createPublisher(publisherRepository);

        mockMvc.perform(delete("/publishers/" + publisher.getId()))
                .andExpect(status().isNoContent());
    }

    private static void validate(PublisherCreateRequest request, ResultActions resultActions) throws Exception {
        validate(request.getName(), resultActions);
    }

    private static void validate(PublisherUpdateRequest request, ResultActions resultActions) throws Exception {
        validate(request.getName(), resultActions);
    }

    private static void validate(Publisher publisher, ResultActions resultActions) throws Exception {
        validate(publisher.getName(), resultActions);
    }

    private static void validate(String name, ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)));
    }
}