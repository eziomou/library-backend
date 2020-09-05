package pl.zmudzin.library.spring.catalog.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.zmudzin.library.core.application.catalog.publisher.PublisherData;
import pl.zmudzin.library.core.application.catalog.publisher.PublisherQuery;
import pl.zmudzin.library.core.application.catalog.publisher.PublisherService;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.domain.catalog.publisher.Publisher;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherId;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.spring.common.RestPagination;
import pl.zmudzin.library.spring.security.Role;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PublisherControllerTest {

    private static final Publisher PUBLISHER = new Publisher(PublisherId.of("1"), "Foo");

    private static final PublisherData PUBLISHER_DATA = new PublisherData(PUBLISHER.getId().toString(), PUBLISHER.getName());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PublisherService publisherService;

    @Test
    void addPublisher_unauthenticated_returnsUnauthorized() throws Exception {
        AddPublisherRequest request = new AddPublisherRequest("Foo");

        mockMvc.perform(post("/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void addPublisher_asMember_returnsForbidden() throws Exception {
        AddPublisherRequest request = new AddPublisherRequest("Foo");

        mockMvc.perform(post("/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void addPublisher_asLibrarian_returnsOk() throws Exception {
        AddPublisherRequest request = new AddPublisherRequest("Foo");

        doNothing().when(publisherService).addPublisher(request.getName());

        mockMvc.perform(post("/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getPublisherById_existingPublisher_returnsOk() throws Exception {
        when(publisherService.getPublisherById(PUBLISHER.getId().toString())).thenReturn(PUBLISHER_DATA);

        mockMvc.perform(get("/publishers/" + PUBLISHER.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PUBLISHER_DATA.getName())));
    }

    @Test
    void getAllPublishersByQuery_validRequest_returnsOk() throws Exception {
        PublisherQuery query = new RestPublisherQuery();
        Pagination pagination = new RestPagination();

        when(publisherService.getAllPublishersByQuery(query, pagination)).thenReturn(Paginated.of(PUBLISHER_DATA));

        mockMvc.perform(get("/publishers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].name", is(PUBLISHER_DATA.getName())));
    }

    @Test
    void updatePublisher_unauthenticated_returnsUnauthorized() throws Exception {
        UpdatePublisherRequest request = new UpdatePublisherRequest("Bar");

        mockMvc.perform(put("/publishers/" + PUBLISHER.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void updatePublisher_asMember_returnsForbidden() throws Exception {
        UpdatePublisherRequest request = new UpdatePublisherRequest("Bar");

        mockMvc.perform(put("/publishers/" + PUBLISHER.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void updatePublisher_asLibrarian_returnsOk() throws Exception {
        UpdatePublisherRequest request = new UpdatePublisherRequest("Bar");

        doNothing().when(publisherService).updatePublisher(PUBLISHER.getId().toString(), request.getName());

        mockMvc.perform(put("/publishers/" + PUBLISHER.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void removePublisher_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(delete("/publishers/" + PUBLISHER.getId()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.MEMBER})
    @Test
    void removePublisher_asMember_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/publishers/" + PUBLISHER.getId()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {Role.WithoutPrefix.LIBRARIAN})
    @Test
    void removePublisher_asLibrarian_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/publishers/" + PUBLISHER.getId()))
                .andExpect(status().isNoContent());
    }
}