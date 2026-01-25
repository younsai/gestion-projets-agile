package io.younsai.gestionprojetsagile.mapper;

import io.younsai.gestionprojetsagile.dto.ProductBacklogDTO;
import io.younsai.gestionprojetsagile.model.ProductBacklog;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EpicMapper.class})
public interface ProductBacklogMapper {
    ProductBacklogDTO toDto(ProductBacklog productBacklog);
    ProductBacklog toEntity(ProductBacklogDTO productBacklogDTO);

    List<ProductBacklogDTO> toDtoList(List<ProductBacklog> productBacklogs);
    List<ProductBacklog> toEntityList(List<ProductBacklogDTO> productBacklogDTOS);

}
