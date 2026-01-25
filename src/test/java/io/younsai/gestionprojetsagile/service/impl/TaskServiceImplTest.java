package io.younsai.gestionprojetsagile.service.impl;

import io.younsai.gestionprojetsagile.model.Task;
import io.younsai.gestionprojetsagile.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl service;

    private Task t;

    @BeforeEach
    void setUp() {
        t = Task.builder().titre("T1").description("d").statut(1).build();
        t.setId(1L);
    }

    @Test
    void createTask_happy() {
        when(taskRepository.save(any())).thenReturn(t);
        Task out = service.createTask(new Task());
        assertThat(out).isNotNull();
        verify(taskRepository).save(any());
    }

    @Test
    void getTask_notFound() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getTask(2L)).isInstanceOf(java.util.NoSuchElementException.class);
    }

    @Test
    void updateTask_happy() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));
        when(taskRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Task patch = Task.builder().titre("NewT").build();
        Task out = service.updateTask(1L, patch);
        assertThat(out.getTitre()).isEqualTo("NewT");
    }
}

