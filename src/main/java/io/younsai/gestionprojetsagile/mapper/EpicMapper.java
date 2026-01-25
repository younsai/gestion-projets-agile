package io.younsai.gestionprojetsagile.mapper;

import io.younsai.gestionprojetsagile.dto.EpicDTO;
import io.younsai.gestionprojetsagile.model.Epic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserStoryMapper.class})
public interface EpicMapper {
    EpicDTO toDto(Epic epic);
    Epic toEntity(EpicDTO epicDTO);
    List<EpicDTO> toDtoList(List<Epic> epics);
    List<Epic> toEntityList(List<EpicDTO> epics);

}
