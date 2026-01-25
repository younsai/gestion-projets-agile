package io.younsai.gestionprojetsagile.mapper;

import io.younsai.gestionprojetsagile.dto.SprintBacklogDTO;
import io.younsai.gestionprojetsagile.model.SprintBacklog;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserStoryMapper.class, TaskMapper.class})
public interface SprintBacklogMapper {
    SprintBacklogDTO toDto(SprintBacklog sprintBacklog);
    SprintBacklog toEntity(SprintBacklogDTO sprintBacklogDTO);

    List<SprintBacklogDTO> toDtoList(List<SprintBacklog> sprintBacklogs);
    List<SprintBacklog> toEntityList(List<SprintBacklogDTO> sprintBacklogDTOS);
}
