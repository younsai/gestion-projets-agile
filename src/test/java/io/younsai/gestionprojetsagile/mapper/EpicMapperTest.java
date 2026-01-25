package io.younsai.gestionprojetsagile.mapper;

import io.younsai.gestionprojetsagile.dto.EpicDTO;
import io.younsai.gestionprojetsagile.dto.UserStoryDTO;
import io.younsai.gestionprojetsagile.model.Epic;
import io.younsai.gestionprojetsagile.model.UserStory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EpicMapperTest {

    private final EpicMapper mapper = MapperTestUtil.initMapper(EpicMapper.class);

    @Test
    public void toDto_maps_basic_fields_and_userStories() {
        UserStory u1 = UserStory.builder().titre("US-A").description("desc A").build();
        UserStory u2 = UserStory.builder().titre("US-B").description("desc B").build();
        Epic epic = Epic.builder().titre("Epic 1").description("An epic").listeDesUserStories(Arrays.asList(u1, u2)).build();

        EpicDTO dto = mapper.toDto(epic);

        assertThat(dto).isNotNull();
        assertThat(dto.getTitre()).isEqualTo("Epic 1");
        assertThat(dto.getDescription()).isEqualTo("An epic");
        assertThat(dto.getListeDesUserStories()).hasSize(2);
        assertThat(dto.getListeDesUserStories()).extracting(UserStoryDTO::getTitre).containsExactly("US-A", "US-B");
    }

    @Test
    public void toEntity_maps_basic_fields_and_userStories() {
        UserStoryDTO us1 = UserStoryDTO.builder().titre("US-X").description("dx").build();
        UserStoryDTO us2 = UserStoryDTO.builder().titre("US-Y").description("dy").build();
        EpicDTO dto = EpicDTO.builder().titre("Epic DTO").description("desc").listeDesUserStories(Arrays.asList(us1, us2)).build();

        Epic entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getTitre()).isEqualTo("Epic DTO");
        assertThat(entity.getDescription()).isEqualTo("desc");
        assertThat(entity.getListeDesUserStories()).hasSize(2);
        assertThat(entity.getListeDesUserStories()).extracting(UserStory::getTitre).containsExactly("US-X", "US-Y");
    }


}
