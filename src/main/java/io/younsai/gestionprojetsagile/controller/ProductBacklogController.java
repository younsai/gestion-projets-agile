package io.younsai.gestionprojetsagile.controller;

import io.younsai.gestionprojetsagile.dto.ProductBacklogDTO;
import io.younsai.gestionprojetsagile.dto.UserStoryDTO;
import io.younsai.gestionprojetsagile.mapper.ProductBacklogMapper;
import io.younsai.gestionprojetsagile.mapper.UserStoryMapper;
import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.service.ProductBacklogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product-backlogs")
public class ProductBacklogController {

    private final ProductBacklogService productBacklogService;
    private final ProductBacklogMapper productBacklogMapper;
    private final UserStoryMapper userStoryMapper;

    public ProductBacklogController(ProductBacklogService productBacklogService, ProductBacklogMapper productBacklogMapper, UserStoryMapper userStoryMapper) {
        this.productBacklogService = productBacklogService;
        this.productBacklogMapper = productBacklogMapper;
        this.userStoryMapper = userStoryMapper;
    }

    // Create
    @PostMapping
    public ResponseEntity<ProductBacklogDTO> createProductBacklog(@RequestBody ProductBacklogDTO dto) {
        ProductBacklog entity = productBacklogMapper.toEntity(dto);
        ProductBacklog saved = productBacklogService.createProductBacklog(entity);
        ProductBacklogDTO out = productBacklogMapper.toDto(saved);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(out);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<ProductBacklogDTO>> listAll(@RequestParam(value = "name", required = false) String name,
                                                             @RequestParam(value = "sortAsc", required = false) Boolean sortAsc) {
        List<ProductBacklog> list;
        if (name != null) {
            list = productBacklogService.filterByName(name);
        } else if (sortAsc != null) {
            list = productBacklogService.sortByName(sortAsc);
        } else {
            list = productBacklogService.listAll();
        }
        List<ProductBacklogDTO> dtos = list.stream().map(productBacklogMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Read single
    @GetMapping("/{id}")
    public ResponseEntity<ProductBacklogDTO> getById(@PathVariable Long id) {
        ProductBacklog pb = productBacklogService.getProductBacklog(id);
        return ResponseEntity.ok(productBacklogMapper.toDto(pb));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ProductBacklogDTO> update(@PathVariable Long id, @RequestBody ProductBacklogDTO dto) {
        ProductBacklog entity = productBacklogMapper.toEntity(dto);
        ProductBacklog updated = productBacklogService.updateProductBacklog(id, entity);
        return ResponseEntity.ok(productBacklogMapper.toDto(updated));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productBacklogService.removeProductBacklog(id);
        return ResponseEntity.noContent().build();
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<ProductBacklogDTO>> search(@RequestParam(value = "q", required = false) String q) {
        List<ProductBacklog> results = productBacklogService.search(q);
        List<ProductBacklogDTO> dtos = results.stream().map(productBacklogMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // UserStory operations
    @PostMapping("/{backlogId}/user-stories")
    public ResponseEntity<UserStoryDTO> addUserStory(@PathVariable Long backlogId, @RequestBody UserStoryDTO dto) {
        UserStory entity = userStoryMapper.toEntity(dto);
        UserStory saved = productBacklogService.addUserStory(backlogId, entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(userStoryMapper.toDto(saved));
    }

    @PutMapping("/{backlogId}/user-stories/{userStoryId}")
    public ResponseEntity<UserStoryDTO> updateUserStory(@PathVariable Long backlogId, @PathVariable Long userStoryId, @RequestBody UserStoryDTO dto) {
        UserStory entity = userStoryMapper.toEntity(dto);
        entity.setId(userStoryId);
        UserStory updated = productBacklogService.updateUserStory(backlogId, entity);
        return ResponseEntity.ok(userStoryMapper.toDto(updated));
    }

    @DeleteMapping("/{backlogId}/user-stories/{userStoryId}")
    public ResponseEntity<Void> deleteUserStory(@PathVariable Long backlogId, @PathVariable Long userStoryId) {
        productBacklogService.removeUserStory(backlogId, userStoryId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{backlogId}/user-stories/{userStoryId}/link-epic/{epicId}")
    public ResponseEntity<UserStoryDTO> linkUserStoryToEpic(@PathVariable Long backlogId, @PathVariable Long userStoryId, @PathVariable Long epicId) {
        UserStory saved = productBacklogService.linkUserStoryToEpic(backlogId, userStoryId, epicId);
        return ResponseEntity.ok(userStoryMapper.toDto(saved));
    }
}
