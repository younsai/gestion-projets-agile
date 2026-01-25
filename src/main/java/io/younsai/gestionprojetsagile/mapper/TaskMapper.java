package io.younsai.gestionprojetsagile.mapper;

import io.younsai.gestionprojetsagile.dto.TaskDTO;
import io.younsai.gestionprojetsagile.model.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserStoryMapper.class})
public interface TaskMapper {
    TaskDTO toDto(Task task);
    Task toEntity(TaskDTO taskDTO);

    List<TaskDTO> toDtoList(List<Task> tasks);
    List<Task> toEntityList(List<TaskDTO> taskDTOS);
}
