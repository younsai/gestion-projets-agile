package io.younsai.gestionprojetsagile.mapper;

import io.younsai.gestionprojetsagile.dto.UserStoryDTO;
import io.younsai.gestionprojetsagile.model.UserStory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EpicMapper.class, SprintBacklogMapper.class})
public interface UserStoryMapper {
    @Mapping(target = "epicLie", ignore = true)
    @Mapping(target = "sprintBacklog", ignore = true)
    UserStoryDTO toDto(UserStory userStory);

    @Mapping(target = "epicLie", ignore = true)
    @Mapping(target = "sprintBacklog", ignore = true)
    UserStory toEntity(UserStoryDTO userStoryDTO);

    List<UserStoryDTO> toDtoList(List<UserStory> userStories);
    List<UserStory> toEntityList(List<UserStoryDTO> userStoryDTOS);
}
