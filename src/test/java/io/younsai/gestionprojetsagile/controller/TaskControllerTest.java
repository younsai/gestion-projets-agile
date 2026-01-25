package io.younsai.gestionprojetsagile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.younsai.gestionprojetsagile.dto.TaskDTO;
import io.younsai.gestionprojetsagile.mapper.TaskMapper;
import io.younsai.gestionprojetsagile.model.Task;
import io.younsai.gestionprojetsagile.service.TaskService;
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
class TaskControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TaskService taskService;
    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createTask_shouldReturnCreated() throws Exception {
        TaskDTO dto = TaskDTO.builder().titre("T1").description("d").build();
        Task t = Task.builder().titre("T1").description("d").statut(1).build();
        t.setId(1L);

        when(taskMapper.toEntity(any())).thenReturn(t);
        when(taskService.createTask(any())).thenReturn(t);
        when(taskMapper.toDto(any())).thenReturn(dto);

        mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titre").value("T1"));
    }

    @Test
    void listAll_shouldReturnArray() throws Exception {
        Task t1 = Task.builder().titre("T1").build(); t1.setId(1L);
        Task t2 = Task.builder().titre("T2").build(); t2.setId(2L);
        TaskDTO d1 = TaskDTO.builder().titre("T1").build();
        TaskDTO d2 = TaskDTO.builder().titre("T2").build();

        when(taskService.listAll()).thenReturn(List.of(t1, t2));
        when(taskMapper.toDto(t1)).thenReturn(d1);
        when(taskMapper.toDto(t2)).thenReturn(d2);

        mockMvc.perform(get("/tasks").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getById_shouldReturnItem() throws Exception {
        Task t = Task.builder().titre("T-get").build(); t.setId(5L);
        TaskDTO dto = TaskDTO.builder().titre("T-get").build();

        when(taskService.getTask(5L)).thenReturn(t);
        when(taskMapper.toDto(t)).thenReturn(dto);

        mockMvc.perform(get("/tasks/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("T-get"));
    }

    @Test
    void updateTask_shouldReturnUpdated() throws Exception {
        TaskDTO dto = TaskDTO.builder().titre("T-upd").build();
        Task entity = Task.builder().titre("T-upd").build(); entity.setId(6L);

        when(taskMapper.toEntity(any())).thenReturn(entity);
        when(taskService.updateTask(eq(6L), any())).thenReturn(entity);
        when(taskMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(put("/tasks/6").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("T-upd"));
    }

    @Test
    void deleteTask_shouldReturnNoContent() throws Exception {
        doNothing().when(taskService).removeTask(7L);
        mockMvc.perform(delete("/tasks/7")).andExpect(status().isNoContent());
    }
}
