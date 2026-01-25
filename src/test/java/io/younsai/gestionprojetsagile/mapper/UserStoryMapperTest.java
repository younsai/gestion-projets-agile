// java
package io.younsai.gestionprojetsagile.mapper;

import io.younsai.gestionprojetsagile.dto.EpicDTO;
import io.younsai.gestionprojetsagile.dto.SprintBacklogDTO;
import io.younsai.gestionprojetsagile.dto.UserStoryDTO;
import io.younsai.gestionprojetsagile.model.Epic;
import io.younsai.gestionprojetsagile.model.SprintBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserStoryMapperTest {

    private final UserStoryMapper mapper = MapperTestUtil.initMapper(UserStoryMapper.class);

    @Test
    public void toDto_maps_basic_fields() {
        UserStory entity = UserStory.builder()
                .titre("US-1")
                .description("First user story")
                .priorite("HIGH")
                .statut("OPEN")
                .epicLie(new Epic())
                .sprintBacklog(new SprintBacklog())
                .build();

        UserStoryDTO dto = mapper.toDto(entity);

    assertThat(dto).isNotNull();
    assertThat(dto)
    .extracting(UserStoryDTO::getTitre, UserStoryDTO::getDescription)
    .containsExactly("US-1", "First user story");
    }

    @Test
    public void toEntity_maps_basic_fields() {
        UserStoryDTO dto = UserStoryDTO.builder()
                .titre("US-2")
                .description("Second user story")
                .priorite("HIGH")
                .statut("OPEN")
                .epicLie(new EpicDTO())
                .sprintBacklog(new SprintBacklogDTO())
                .build();

        UserStory entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getTitre()).isEqualTo("US-2");
        assertThat(entity.getDescription()).isEqualTo("Second user story");
    }

    @Test
    public void toDtoList_maps_list_and_preserves_size() {
        UserStory u1 = UserStory.builder().titre("A").description("A desc").build();
        UserStory u2 = UserStory.builder().titre("B").description("B desc").build();

        List<UserStory> input = Arrays.asList(u1, u2);
        List<UserStoryDTO> output = mapper.toDtoList(input);

        assertThat(output).isNotNull();
        assertThat(output).hasSize(2);
        assertThat(output.get(0).getTitre()).isEqualTo("A");
        assertThat(output.get(1).getTitre()).isEqualTo("B");
    }
}
