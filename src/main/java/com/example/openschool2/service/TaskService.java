package com.example.openschool2.service;

import com.example.openschool2.model.Task;

import java.util.List;

public interface TaskService {
    Task create(Task task);
    Task findTaskById(Long taskId);
    List<Task> findAll();
    Task update(Task task);
    void delete(Long taskId);
}
