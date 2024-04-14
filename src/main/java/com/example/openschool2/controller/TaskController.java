package com.example.openschool2.controller;

import com.example.openschool2.dto.TaskRequestDto;
import com.example.openschool2.dto.TaskResponseDto;
import com.example.openschool2.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping({"", "/"})
    public TaskResponseDto create(@Valid @RequestBody TaskRequestDto task) {
        return taskService.create(task);
    }

    @GetMapping("/{id}")
    public TaskResponseDto findTaskById(@PathVariable Long id) {
        return taskService.findTaskById(id);
    }

    @GetMapping({"", "/"})
    public List<TaskResponseDto> findAll() {
        return taskService.findAll();
    }

    @PutMapping("/{id}")
    public TaskResponseDto update(@PathVariable Long id, @RequestBody @Valid TaskRequestDto request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
