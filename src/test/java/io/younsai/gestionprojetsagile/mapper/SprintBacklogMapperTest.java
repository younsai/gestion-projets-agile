package io.younsai.gestionprojetsagile.mapper;

import io.younsai.gestionprojetsagile.dto.SprintBacklogDTO;
import io.younsai.gestionprojetsagile.dto.TaskDTO;
import io.younsai.gestionprojetsagile.dto.UserStoryDTO;
import io.younsai.gestionprojetsagile.model.SprintBacklog;
import io.younsai.gestionprojetsagile.model.Task;
import io.younsai.gestionprojetsagile.model.UserStory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class SprintBacklogMapperTest {
    private final SprintBacklogMapper mapper = MapperTestUtil.initMapper(SprintBacklogMapper.class);

    @Test
    public void toDto_maps_basic_fields_and_nested_lists() {
        UserStory us1 = UserStory.builder().titre("US1").description("d1").build();
        Task t1 = Task.builder().titre("T1").description("td1").build();
        SprintBacklog sb = SprintBacklog.builder().nom("SB").listeDesUserStories(Arrays.asList(us1)).listeDesTasks(Arrays.asList(t1)).build();

        SprintBacklogDTO dto = mapper.toDto(sb);

        assertThat(dto).isNotNull();
        assertThat(dto.getNom()).isEqualTo("SB");
        assertThat(dto.getListeDesUserStories()).hasSize(1);
        assertThat(dto.getListeDesTasks()).hasSize(1);
        assertThat(dto.getListeDesUserStories()).extracting(UserStoryDTO::getTitre).containsExactly("US1");
        assertThat(dto.getListeDesTasks()).extracting(TaskDTO::getTitre).containsExactly("T1");
    }

    @Test
    public void toEntity_maps_basic_fields_and_nested_lists() {
        UserStoryDTO usd = UserStoryDTO.builder().titre("USX").description("dx").build();
        TaskDTO td = TaskDTO.builder().titre("TX").description("dtx").build();
        SprintBacklogDTO dto = SprintBacklogDTO.builder().nom("SB-D").listeDesUserStories(Arrays.asList(usd)).listeDesTasks(Arrays.asList(td)).build();

        SprintBacklog entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getNom()).isEqualTo("SB-D");
        assertThat(entity.getListeDesUserStories()).hasSize(1);
        assertThat(entity.getListeDesTasks()).hasSize(1);
        assertThat(entity.getListeDesUserStories()).extracting(UserStory::getTitre).containsExactly("USX");
        assertThat(entity.getListeDesTasks()).extracting(Task::getTitre).containsExactly("TX");
    }


}
