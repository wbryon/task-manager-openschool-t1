package com.example.openschool2.service;

import com.example.openschool2.model.Task;
import com.example.openschool2.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task create(Task task) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Task findTaskById(Long taskId) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return null;
    }

    @Override
    @Transactional
    public Task update(Task task) {
        return null;
    }

    @Override
    @Transactional
    public void delete(Long taskId) {

    }
}
