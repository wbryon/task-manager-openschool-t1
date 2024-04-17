package com.example.openschool2.service;

import com.example.openschool2.dto.TaskRequestDto;
import com.example.openschool2.dto.TaskResponseDto;
import com.example.openschool2.exception.BadRequestException;
import com.example.openschool2.exception.NotFoundException;
import com.example.openschool2.model.Status;
import com.example.openschool2.model.Task;
import com.example.openschool2.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public TaskResponseDto create(TaskRequestDto request) {
        Task task = buildTaskRequest(request);
        return buildTaskResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskResponseDto update(Long taskId, TaskRequestDto request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with ID: " + taskId + " not found"));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        if (isValidStatusUpdate(task.getStatus(), request.getStatus())) {
            task.setStatus(request.getStatus());
        } else {
            throw new BadRequestException("Invalid status transition from " + task.getStatus() + " to " + request.getStatus());
        }

        task = taskRepository.save(task);

        return buildTaskResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDto findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .map(this::buildTaskResponse)
                .orElseThrow(() -> new NotFoundException("Task with ID: " + taskId + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(this::buildTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new NotFoundException("Task with ID: " + taskId + " not found");
        }
        taskRepository.deleteById(taskId);
    }

    private Task buildTaskRequest(TaskRequestDto request) {
        return new Task()
                .setTitle(request.getTitle())
                .setDescription(request.getDescription())
                .setDueDate(request.getDueDate())
                .setStatus(Status.NEW);
    }

    private TaskResponseDto buildTaskResponse(Task task) {
        return new TaskResponseDto()
                .setId(task.getId())
                .setTitle(task.getTitle())
                .setDescription(task.getDescription())
                .setDueDate(task.getDueDate())
                .setStatus(task.getStatus());
    }

    private boolean isValidStatusUpdate(Status currentStatus, Status newStatus) {
        switch (currentStatus) {
            case NEW -> {
                return newStatus == Status.IN_PROGRESS;
            }
            case IN_PROGRESS -> {
                return newStatus == Status.DONE;
            }
            default -> {
                return false;
            }
        }
    }
}
