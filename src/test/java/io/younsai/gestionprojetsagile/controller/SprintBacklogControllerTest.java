package io.younsai.gestionprojetsagile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.younsai.gestionprojetsagile.dto.SprintBacklogDTO;
import io.younsai.gestionprojetsagile.dto.UserStoryDTO;
import io.younsai.gestionprojetsagile.mapper.SprintBacklogMapper;
import io.younsai.gestionprojetsagile.mapper.UserStoryMapper;
import io.younsai.gestionprojetsagile.model.SprintBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.service.SprintBacklogService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SprintBacklogControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private SprintBacklogService sprintBacklogService;
    @Mock
    private SprintBacklogMapper sprintBacklogMapper;
    @Mock
    private UserStoryMapper userStoryMapper;

    @InjectMocks
    private SprintBacklogController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createSprint_shouldReturnCreated() throws Exception {
        SprintBacklogDTO dto = SprintBacklogDTO.builder().nom("SB").build();
        SprintBacklog sb = SprintBacklog.builder().nom("SB").build();
        sb.setId(2L);

        when(sprintBacklogMapper.toEntity(any())).thenReturn(sb);
        when(sprintBacklogService.createSprintBacklog(any())).thenReturn(sb);
        when(sprintBacklogMapper.toDto(any())).thenReturn(dto);

        mockMvc.perform(post("/sprints").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("SB"));
    }

    @Test
    void listAll_shouldReturnArray() throws Exception {
        SprintBacklog s1 = SprintBacklog.builder().nom("S1").build(); s1.setId(1L);
        SprintBacklog s2 = SprintBacklog.builder().nom("S2").build(); s2.setId(2L);
        SprintBacklogDTO d1 = SprintBacklogDTO.builder().nom("S1").build();
        SprintBacklogDTO d2 = SprintBacklogDTO.builder().nom("S2").build();

        when(sprintBacklogService.listAll()).thenReturn(List.of(s1, s2));
        when(sprintBacklogMapper.toDto(s1)).thenReturn(d1);
        when(sprintBacklogMapper.toDto(s2)).thenReturn(d2);

        mockMvc.perform(get("/sprints").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getById_shouldReturnItem() throws Exception {
        SprintBacklog sb = SprintBacklog.builder().nom("SX").build(); sb.setId(5L);
        SprintBacklogDTO dto = SprintBacklogDTO.builder().nom("SX").build();

        when(sprintBacklogService.getSprintBacklog(5L)).thenReturn(sb);
        when(sprintBacklogMapper.toDto(sb)).thenReturn(dto);

        mockMvc.perform(get("/sprints/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("SX"));
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        SprintBacklogDTO dto = SprintBacklogDTO.builder().nom("Updated").build();
        SprintBacklog entity = SprintBacklog.builder().nom("Updated").build(); entity.setId(6L);

        when(sprintBacklogMapper.toEntity(any())).thenReturn(entity);
        when(sprintBacklogService.updateSprintBacklog(eq(6L), any())).thenReturn(entity);
        when(sprintBacklogMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(put("/sprints/6").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Updated"));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        doNothing().when(sprintBacklogService).removeSprintBacklog(7L);

        mockMvc.perform(delete("/sprints/7")).andExpect(status().isNoContent());
    }

    @Test
    void importFromProductBacklog_shouldReturnList() throws Exception {
        UserStory us = UserStory.builder().titre("US1").build(); us.setId(100L);
        UserStoryDTO dto = UserStoryDTO.builder().titre("US1").build();

        when(sprintBacklogService.addUserStoriesFromProductBacklog(eq(1L), eq(10L), any())).thenReturn(List.of(us));
        when(userStoryMapper.toDto(us)).thenReturn(dto);

        mockMvc.perform(post("/sprints/1/import-from-product-backlog/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(100L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titre").value("US1"));
    }
}
