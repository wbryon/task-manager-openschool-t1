package com.example.openschool2.service;

import com.example.openschool2.dto.TaskRequestDto;
import com.example.openschool2.dto.TaskResponseDto;
import com.example.openschool2.exception.NotFoundException;
import com.example.openschool2.model.Status;
import com.example.openschool2.model.Task;
import com.example.openschool2.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskRequestDto requestDto;
    @BeforeEach
    public void createTasksForTest() {
        requestDto = new TaskRequestDto()
                .setId(1L)
                .setTitle("Test task1")
                .setDescription("Test description1")
                .setDueDate(LocalDateTime.now().plusDays(1))
                .setStatus(Status.NEW);
    }

    @Test
    void testCreateTask() {

        Task task = new Task()
                .setId(requestDto.getId())
                .setTitle(requestDto.getTitle())
                .setDescription(requestDto.getDescription())
                .setDueDate(requestDto.getDueDate())
                .setStatus(requestDto.getStatus());

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDto result = taskService.create(requestDto);

        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(task.getDueDate(), result.getDueDate());
        assertEquals(task.getStatus(), result.getStatus());
    }

    @Test
    void testFindTaskById() {
        Task existingTask = new Task()
                .setId(requestDto.getId())
                .setTitle(requestDto.getTitle())
                .setDescription(requestDto.getDescription())
                .setDueDate(requestDto.getDueDate())
                .setStatus(requestDto.getStatus());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        TaskResponseDto responseDto = taskService.findTaskById(1L);

        assertNotNull(responseDto);
        assertEquals(existingTask.getId(), responseDto.getId());
        assertEquals(existingTask.getTitle(), responseDto.getTitle());
        assertEquals(existingTask.getStatus(), responseDto.getStatus());
    }

    @Test
    void testFindTaskByIdNotFound() {
        Long taskId = 999L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.findTaskById(taskId));
    }

    @Test
    void testUpdateTask() {
        Long taskId = 1L;
        TaskRequestDto requestDtoUpdated = new TaskRequestDto()
                .setTitle("Updated Task")
                .setStatus(Status.IN_PROGRESS);

        Task existingTask = new Task()
                .setId(requestDto.getId())
                .setTitle(requestDto.getTitle())
                .setDescription(requestDto.getDescription())
                .setDueDate(requestDto.getDueDate())
                .setStatus(requestDto.getStatus());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        TaskResponseDto updatedDto = taskService.update(taskId, requestDtoUpdated);

        assertNotNull(updatedDto);
        assertEquals(taskId, updatedDto.getId());
        assertEquals(requestDtoUpdated.getTitle(), updatedDto.getTitle());
        assertEquals(Status.IN_PROGRESS, updatedDto.getStatus());
    }

    @Test
    void testDeleteTask() {
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(taskId);

        assertDoesNotThrow(() -> taskService.delete(taskId));
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void testDeleteNonExistentTask() {
        Long taskId = 999L;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> taskService.delete(taskId));
    }
}