package com.example.openschool2.service;

import com.example.openschool2.dto.TaskRequestDto;
import com.example.openschool2.dto.TaskResponseDto;

import java.util.List;

public interface TaskService {
    TaskResponseDto create(TaskRequestDto task);
    TaskResponseDto findTaskById(Long taskId);
    List<TaskResponseDto> findAll();
    TaskResponseDto update(Long taskId, TaskRequestDto request);
    void delete(Long taskId);
}
