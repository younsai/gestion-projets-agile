package io.younsai.gestionprojetsagile.service.impl;

import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.model.SprintBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.repository.SprintBacklogRepository;
import io.younsai.gestionprojetsagile.repository.TaskRepository;
import io.younsai.gestionprojetsagile.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SprintBacklogServiceImplTest {

    @Mock
    private SprintBacklogRepository sprintRepo;
    @Mock
    private UserStoryRepository userStoryRepo;
    @Mock
    private TaskRepository taskRepo;
    @Mock
    private io.younsai.gestionprojetsagile.repository.ProductBacklogRepository productBacklogRepo;

    @InjectMocks
    private SprintBacklogServiceImpl service;

    private SprintBacklog sb;
    private ProductBacklog pb;
    private UserStory us1;

    @BeforeEach
    void setUp() {
        sb = SprintBacklog.builder().nom("SB").build();
        sb.setId(1L);
        sb.setListeDesUserStories(new ArrayList<>());

        pb = ProductBacklog.builder().nom("PB").build();
        pb.setId(10L);

        us1 = UserStory.builder().titre("US1").build();
        us1.setId(100L);
        us1.setProductBacklog(pb);
    }

    @Test
    void createSprintBacklog_happyPath() {
        when(sprintRepo.save(any())).thenReturn(sb);
        SprintBacklog out = service.createSprintBacklog(new SprintBacklog());
        assertThat(out).isNotNull();
        verify(sprintRepo).save(any());
    }

    @Test
    void getSprintBacklog_found() {
        when(sprintRepo.findById(1L)).thenReturn(Optional.of(sb));
        SprintBacklog out = service.getSprintBacklog(1L);
        assertThat(out.getId()).isEqualTo(1L);
    }

    @Test
    void getSprintBacklog_notFound() {
        when(sprintRepo.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getSprintBacklog(2L)).isInstanceOf(java.util.NoSuchElementException.class);
    }

    @Test
    void importUserStories_happyPath() {
        when(sprintRepo.findById(1L)).thenReturn(Optional.of(sb));
        when(productBacklogRepo.findById(10L)).thenReturn(Optional.of(pb));
        when(userStoryRepo.findById(100L)).thenReturn(Optional.of(us1));
        when(userStoryRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        List<UserStory> moved = service.addUserStoriesFromProductBacklog(1L, 10L, List.of(100L));
        assertThat(moved).hasSize(1);
        verify(sprintRepo).save(any());
    }
}

