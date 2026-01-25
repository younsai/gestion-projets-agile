package io.younsai.gestionprojetsagile.mapper;

import io.younsai.gestionprojetsagile.dto.ProductBacklogDTO;
import io.younsai.gestionprojetsagile.dto.EpicDTO;
import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.model.Epic;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductBacklogMapperTest {
    private final ProductBacklogMapper mapper = MapperTestUtil.initMapper(ProductBacklogMapper.class);

    @Test
    public void toDto_maps_basic_fields_and_epics() {
        Epic e1 = Epic.builder().titre("E1").description("d1").build();
        Epic e2 = Epic.builder().titre("E2").description("d2").build();
        ProductBacklog pb = ProductBacklog.builder().nom("PB").listeDesEpics(Arrays.asList(e1, e2)).build();

        ProductBacklogDTO dto = mapper.toDto(pb);

        assertThat(dto).isNotNull();
        assertThat(dto.getNom()).isEqualTo("PB");
        assertThat(dto.getListeDesEpics()).hasSize(2);
        assertThat(dto.getListeDesEpics()).extracting(EpicDTO::getTitre).containsExactly("E1", "E2");
    }

    @Test
    public void toEntity_maps_basic_fields_and_epics() {
        EpicDTO ed1 = EpicDTO.builder().titre("ED1").description("dd1").build();
        EpicDTO ed2 = EpicDTO.builder().titre("ED2").description("dd2").build();
        ProductBacklogDTO dto = ProductBacklogDTO.builder().nom("PB-D").listeDesEpics(Arrays.asList(ed1, ed2)).build();

        ProductBacklog entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getNom()).isEqualTo("PB-D");
        assertThat(entity.getListeDesEpics()).hasSize(2);
        assertThat(entity.getListeDesEpics()).extracting(Epic::getTitre).containsExactly("ED1", "ED2");
    }

}
