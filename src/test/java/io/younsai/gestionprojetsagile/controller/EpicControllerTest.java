package io.younsai.gestionprojetsagile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.younsai.gestionprojetsagile.dto.EpicDTO;
import io.younsai.gestionprojetsagile.mapper.EpicMapper;
import io.younsai.gestionprojetsagile.model.Epic;
import io.younsai.gestionprojetsagile.service.EpicService;
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
class EpicControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EpicService epicService;
    @Mock
    private EpicMapper epicMapper;

    @InjectMocks
    private EpicController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createEpic_shouldReturnCreated() throws Exception {
        EpicDTO dto = EpicDTO.builder().titre("E1").description("desc").build();
        Epic entity = Epic.builder().titre("E1").description("desc").build();
        entity.setId(1L);

        when(epicMapper.toEntity(any())).thenReturn(entity);
        when(epicService.createEpic(any())).thenReturn(entity);
        when(epicMapper.toDto(any())).thenReturn(dto);

        mockMvc.perform(post("/epics").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titre").value("E1"));
    }

    @Test
    void listAll_shouldReturnArray() throws Exception {
        Epic e1 = Epic.builder().titre("E1").build(); e1.setId(1L);
        Epic e2 = Epic.builder().titre("E2").build(); e2.setId(2L);
        EpicDTO d1 = EpicDTO.builder().titre("E1").build();
        EpicDTO d2 = EpicDTO.builder().titre("E2").build();

        when(epicService.listAll()).thenReturn(List.of(e1, e2));
        when(epicMapper.toDto(e1)).thenReturn(d1);
        when(epicMapper.toDto(e2)).thenReturn(d2);

        mockMvc.perform(get("/epics").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getById_shouldReturnItem() throws Exception {
        Epic e = Epic.builder().titre("E-get").build(); e.setId(5L);
        EpicDTO dto = EpicDTO.builder().titre("E-get").build();

        when(epicService.getEpic(5L)).thenReturn(e);
        when(epicMapper.toDto(e)).thenReturn(dto);

        mockMvc.perform(get("/epics/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("E-get"));
    }

    @Test
    void update_shouldReturnUpdated() throws Exception {
        EpicDTO dto = EpicDTO.builder().titre("E-upd").description("d").build();
        Epic entity = Epic.builder().titre("E-upd").description("d").build(); entity.setId(6L);

        when(epicMapper.toEntity(any())).thenReturn(entity);
        when(epicService.updateEpic(eq(6L), any())).thenReturn(entity);
        when(epicMapper.toDto(entity)).thenReturn(dto);

        mockMvc.perform(put("/epics/6").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("E-upd"));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        doNothing().when(epicService).removeEpic(7L);
        mockMvc.perform(delete("/epics/7")).andExpect(status().isNoContent());
    }
}

