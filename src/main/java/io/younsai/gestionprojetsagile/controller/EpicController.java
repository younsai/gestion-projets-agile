package io.younsai.gestionprojetsagile.controller;

import io.younsai.gestionprojetsagile.dto.EpicDTO;
import io.younsai.gestionprojetsagile.mapper.EpicMapper;
import io.younsai.gestionprojetsagile.model.Epic;
import io.younsai.gestionprojetsagile.service.EpicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/epics")
public class EpicController {

    private final EpicService epicService;
    private final EpicMapper epicMapper;

    public EpicController(EpicService epicService, EpicMapper epicMapper) {
        this.epicService = epicService;
        this.epicMapper = epicMapper;
    }

    @PostMapping
    public ResponseEntity<EpicDTO> create(@RequestBody EpicDTO dto) {
        try {
            Epic e = epicMapper.toEntity(dto);
            Epic saved = epicService.createEpic(e);
            return ResponseEntity.status(201).body(epicMapper.toDto(saved));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EpicDTO>> listAll() {
        List<Epic> all = epicService.listAll();
        return ResponseEntity.ok(all.stream().map(epicMapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpicDTO> getById(@PathVariable Long id) {
        try {
            Epic e = epicService.getEpic(id);
            return ResponseEntity.ok(epicMapper.toDto(e));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EpicDTO> update(@PathVariable Long id, @RequestBody EpicDTO dto) {
        try {
            Epic e = epicMapper.toEntity(dto);
            Epic updated = epicService.updateEpic(id, e);
            return ResponseEntity.ok(epicMapper.toDto(updated));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            epicService.removeEpic(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
