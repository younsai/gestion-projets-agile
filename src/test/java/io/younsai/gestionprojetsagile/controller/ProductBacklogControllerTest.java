package io.younsai.gestionprojetsagile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.younsai.gestionprojetsagile.dto.ProductBacklogDTO;
import io.younsai.gestionprojetsagile.mapper.ProductBacklogMapper;
import io.younsai.gestionprojetsagile.mapper.UserStoryMapper;
import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.service.ProductBacklogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductBacklogControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ProductBacklogService productBacklogService;

    @Mock
    private ProductBacklogMapper productBacklogMapper;

    @Mock
    private UserStoryMapper userStoryMapper;

    @InjectMocks
    private ProductBacklogController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createProductBacklog_shouldReturnCreated() throws Exception {
        ProductBacklogDTO dto = ProductBacklogDTO.builder().nom("PB-Test").build();
        ProductBacklog entity = ProductBacklog.builder().nom("PB-Test").build();
        entity.setId(42L);

        when(productBacklogMapper.toEntity(any())).thenReturn(entity);
        when(productBacklogService.createProductBacklog(any())).thenReturn(entity);
        when(productBacklogMapper.toDto(any())).thenReturn(dto);

        mockMvc.perform(post("/product-backlogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nom").value("PB-Test"));
    }

    @Test
    void listAll_shouldReturnArray() throws Exception {
        ProductBacklog p1 = ProductBacklog.builder().nom("A").build(); p1.setId(1L);
        ProductBacklog p2 = ProductBacklog.builder().nom("B").build(); p2.setId(2L);
        ProductBacklogDTO d1 = ProductBacklogDTO.builder().nom("A").build();
        ProductBacklogDTO d2 = ProductBacklogDTO.builder().nom("B").build();

        when(productBacklogService.listAll()).thenReturn(List.of(p1, p2));
        when(productBacklogMapper.toDto(p1)).thenReturn(d1);
        when(productBacklogMapper.toDto(p2)).thenReturn(d2);

        mockMvc.perform(get("/product-backlogs").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getById_shouldReturnItem() throws Exception {
        ProductBacklog p = ProductBacklog.builder().nom("X").build(); p.setId(5L);
        ProductBacklogDTO dto = ProductBacklogDTO.builder().nom("X").build();

        when(productBacklogService.getProductBacklog(5L)).thenReturn(p);
        when(productBacklogMapper.toDto(p)).thenReturn(dto);

        mockMvc.perform(get("/product-backlogs/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("X"));
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        ProductBacklogDTO dto = ProductBacklogDTO.builder().nom("Updated").build();
        ProductBacklog entity = ProductBacklog.builder().nom("Updated").build(); entity.setId(6L);

        when(productBacklogMapper.toEntity(any())).thenReturn(entity);
        when(productBacklogService.updateProductBacklog(eq(6L), any())).thenReturn(entity);
        when(productBacklogMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(put("/product-backlogs/6").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Updated"));
    }

    @Test
    void delete_shouldReturnNoContent_andCallService() throws Exception {
        doNothing().when(productBacklogService).removeProductBacklog(7L);

        mockMvc.perform(delete("/product-backlogs/7")).andExpect(status().isNoContent());

        verify(productBacklogService).removeProductBacklog(7L);
    }

    @Test
    void addUserStory_shouldReturnCreated() throws Exception {
        UserStory us = UserStory.builder().titre("US1").description("d").build(); us.setId(100L);
        when(userStoryMapper.toEntity(any())).thenReturn(us);
        when(productBacklogService.addUserStory(eq(1L), any())).thenReturn(us);
        when(userStoryMapper.toDto(us)).thenReturn(io.younsai.gestionprojetsagile.dto.UserStoryDTO.builder().titre("US1").build());

        mockMvc.perform(post("/product-backlogs/1/user-stories").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(io.younsai.gestionprojetsagile.dto.UserStoryDTO.builder().titre("US1").build())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titre").value("US1"));
    }

    // --- additional tests: search, filter, sort, update/delete/link user stories ---

    @Test
    void search_shouldReturnResults() throws Exception {
        ProductBacklog p = ProductBacklog.builder().nom("Search").build(); p.setId(8L);
        ProductBacklogDTO dto = ProductBacklogDTO.builder().nom("Search").build();

        when(productBacklogService.search("q")).thenReturn(List.of(p));
        when(productBacklogMapper.toDto(p)).thenReturn(dto);

        mockMvc.perform(get("/product-backlogs/search").param("q", "q").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void filterByName_shouldReturnFiltered() throws Exception {
        ProductBacklog p = ProductBacklog.builder().nom("Filter").build(); p.setId(10L);
        ProductBacklogDTO dto = ProductBacklogDTO.builder().nom("Filter").build();

        when(productBacklogService.filterByName("Filter")).thenReturn(List.of(p));
        when(productBacklogMapper.toDto(p)).thenReturn(dto);

        mockMvc.perform(get("/product-backlogs").param("name","Filter").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void sortByName_shouldReturnSorted() throws Exception {
        ProductBacklog p = ProductBacklog.builder().nom("Sorted").build(); p.setId(11L);
        ProductBacklogDTO dto = ProductBacklogDTO.builder().nom("Sorted").build();

        when(productBacklogService.sortByName(true)).thenReturn(List.of(p));
        when(productBacklogMapper.toDto(p)).thenReturn(dto);

        mockMvc.perform(get("/product-backlogs").param("sortAsc","true").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateUserStory_shouldReturnUpdated() throws Exception {
        io.younsai.gestionprojetsagile.dto.UserStoryDTO dto = io.younsai.gestionprojetsagile.dto.UserStoryDTO.builder().titre("US-upd").build();
        UserStory entity = UserStory.builder().titre("US-upd").build(); entity.setId(200L);

        when(userStoryMapper.toEntity(any())).thenReturn(entity);
        when(productBacklogService.updateUserStory(eq(1L), any())).thenReturn(entity);
        when(userStoryMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(put("/product-backlogs/1/user-stories/200").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("US-upd"));
    }

    @Test
    void deleteUserStory_shouldReturnNoContent_andCallService() throws Exception {
        doNothing().when(productBacklogService).removeUserStory(1L, 300L);

        mockMvc.perform(delete("/product-backlogs/1/user-stories/300")).andExpect(status().isNoContent());

        verify(productBacklogService).removeUserStory(1L, 300L);
    }

    @Test
    void linkUserStoryToEpic_shouldReturnOk() throws Exception {
        UserStory entity = UserStory.builder().titre("US-link").build(); entity.setId(14L);
        io.younsai.gestionprojetsagile.dto.UserStoryDTO dto = io.younsai.gestionprojetsagile.dto.UserStoryDTO.builder().titre("US-link").build();

        when(productBacklogService.linkUserStoryToEpic(1L, 14L, 5L)).thenReturn(entity);
        when(userStoryMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(post("/product-backlogs/1/user-stories/14/link-epic/5")).andExpect(status().isOk()).andExpect(jsonPath("$.titre").value("US-link"));
    }

}
