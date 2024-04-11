package com.example.openschool2.controller;

import com.example.openschool2.model.Task;
import com.example.openschool2.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public Task create(@RequestBody Task task) {
        return taskService.create(task);
    }

    @GetMapping("/{id}")
    public Task findTaskById(@PathVariable Long id) {
        return taskService.findTaskById(id);
    }

    @GetMapping
    public List<Task> findAll() {
        return taskService.findAll();
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id) {
        return taskService.update(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
