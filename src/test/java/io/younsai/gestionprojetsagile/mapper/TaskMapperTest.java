package io.younsai.gestionprojetsagile.mapper;

import io.younsai.gestionprojetsagile.dto.TaskDTO;
import io.younsai.gestionprojetsagile.dto.UserStoryDTO;
import io.younsai.gestionprojetsagile.model.Task;
import io.younsai.gestionprojetsagile.model.UserStory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskMapperTest {
    private final TaskMapper mapper = MapperTestUtil.initMapper(TaskMapper.class);

    @Test
    public void toDto_and_toEntity_map_userStory_reference_as_null_or_ignored() {
        UserStory us = UserStory.builder().titre("UST").description("usd").build();
        Task t = Task.builder().titre("TK").description("td").userStory(us).build();

        TaskDTO dto = mapper.toDto(t);
        assertThat(dto).isNotNull();
        assertThat(dto.getTitre()).isEqualTo("TK");

        Task entity = mapper.toEntity(dto);
        assertThat(entity).isNotNull();
        assertThat(entity.getTitre()).isEqualTo("TK");
    }
}

