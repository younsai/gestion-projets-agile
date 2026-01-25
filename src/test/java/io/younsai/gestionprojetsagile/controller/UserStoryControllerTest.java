package io.younsai.gestionprojetsagile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.younsai.gestionprojetsagile.dto.UserStoryDTO;
import io.younsai.gestionprojetsagile.mapper.UserStoryMapper;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.service.UserStoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserStoryControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserStoryService userStoryService;
    @Mock
    private UserStoryMapper userStoryMapper;

    @InjectMocks
    private UserStoryController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createUserStory_shouldReturnCreated() throws Exception {
        UserStoryDTO dto = UserStoryDTO.builder().titre("US1").description("d").build();
        UserStory us = UserStory.builder().titre("US1").description("d").build();
        us.setId(100L);

        Mockito.when(userStoryMapper.toEntity(any())).thenReturn(us);
        Mockito.when(userStoryService.createUserStory(Mockito.anyLong(), any())).thenReturn(us);
        Mockito.when(userStoryMapper.toDto(any())).thenReturn(dto);

        mockMvc.perform(post("/user-stories?productBacklogId=1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titre").value("US1"));
    }

    @Test
    void updateUserStory_shouldReturnUpdated() throws Exception {
        UserStoryDTO dto = UserStoryDTO.builder().titre("US-upd").build();
        UserStory entity = UserStory.builder().titre("US-upd").build(); entity.setId(200L);

        when(userStoryMapper.toEntity(any())).thenReturn(entity);
        when(userStoryService.updateUserStory(eq(200L), any())).thenReturn(entity);
        when(userStoryMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(put("/user-stories/200").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("US-upd"));
    }

    @Test
    void deleteUserStory_shouldReturnNoContent() throws Exception {
        doNothing().when(userStoryService).removeUserStory(300L);
        mockMvc.perform(delete("/user-stories/300")).andExpect(status().isNoContent());
    }

    @Test
    void linkToEpic_shouldReturnOk() throws Exception {
        UserStory entity = UserStory.builder().titre("US-link").build(); entity.setId(400L);
        UserStoryDTO dto = UserStoryDTO.builder().titre("US-link").build();

        when(userStoryService.linkUserStoryToEpic(400L, 10L)).thenReturn(entity);
        when(userStoryMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(post("/user-stories/400/link-epic/10")).andExpect(status().isOk()).andExpect(jsonPath("$.titre").value("US-link"));
    }
}
