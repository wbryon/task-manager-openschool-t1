package com.example.openschool2.controller;

import com.example.openschool2.dto.TaskRequestDto;
import com.example.openschool2.dto.TaskResponseDto;
import com.example.openschool2.exception.BadRequestException;
import com.example.openschool2.exception.NotFoundException;
import com.example.openschool2.model.Status;
import com.example.openschool2.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskRequestDto requestDto1;
    private TaskRequestDto requestDto2;

    @BeforeEach
    public void createTasksForTest() {
        requestDto1 = new TaskRequestDto()
                .setId(1L)
                .setTitle("Test task1")
                .setDescription("Test description1")
                .setDueDate(LocalDateTime.now().plusDays(1))
                .setStatus(Status.NEW);

        requestDto2 = new TaskRequestDto()
                .setId(2L)
                .setTitle("Test task2")
                .setDescription("Test description2")
                .setDueDate(LocalDateTime.now().plusDays(1))
                .setStatus(Status.NEW);
    }

    @Test
    void testCreateTask() throws Exception {

        TaskResponseDto responseDto = new TaskResponseDto()
                .setId(requestDto1.getId())
                .setTitle(requestDto1.getTitle())
                .setDescription(requestDto1.getDescription())
                .setDueDate(requestDto1.getDueDate())
                .setStatus(requestDto1.getStatus());

        given(taskServiceMock.create(any(TaskRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$.description").value(responseDto.getDescription()))
                .andExpect(jsonPath("$.dueDate").value(responseDto.getDueDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.status").value(responseDto.getStatus().toString()));
    }

    @Test
    void testCreateTaskWithInvalidStatusThrowsError() throws Exception {
        requestDto1.setStatus(Status.IN_PROGRESS);

        given(taskServiceMock.create(argThat(req -> req.getStatus() == Status.IN_PROGRESS)))
                .willThrow(new BadRequestException("New tasks must have the 'NEW' status."));


        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("New tasks must have the 'NEW' status."));
    }

    @Test
    void testFindTaskById() throws Exception {
        TaskResponseDto responseDto = new TaskResponseDto()
                .setId(requestDto1.getId())
                .setTitle(requestDto1.getTitle())
                .setDescription(requestDto1.getDescription())
                .setDueDate(requestDto1.getDueDate())
                .setStatus(requestDto1.getStatus());

        given(taskServiceMock.findTaskById(1L)).willReturn(responseDto);

        mockMvc.perform(get("/api/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$.description").value(responseDto.getDescription()))
                .andExpect(jsonPath("$.dueDate").value(responseDto.getDueDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.status").value(responseDto.getStatus().toString()));
    }

    @Test
    void testFindTaskByNonExistentId() throws Exception {
        Long nonExistentId = 999L;

        given(taskServiceMock.findTaskById(nonExistentId))
                .willThrow(new NotFoundException("Task with ID: " + nonExistentId + " not found"));

        mockMvc.perform(get("/api/tasks/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Task with ID: " + nonExistentId + " not found"));
    }

    @Test
    void testFindAllTasks() throws Exception {
        List<TaskResponseDto> tasks = Arrays.asList(
                new TaskResponseDto()
                        .setId(requestDto1.getId())
                        .setTitle(requestDto1.getTitle())
                        .setDescription(requestDto1.getDescription())
                        .setDueDate(requestDto1.getDueDate())
                        .setStatus(requestDto1.getStatus()),
                new TaskResponseDto()
                        .setId(requestDto2.getId())
                        .setTitle(requestDto2.getTitle())
                        .setDescription(requestDto2.getDescription())
                        .setDueDate(requestDto2.getDueDate())
                        .setStatus(requestDto2.getStatus())
        );

        given(taskServiceMock.findAll()).willReturn(tasks);

        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(tasks.get(0).getId()))
                .andExpect(jsonPath("$[0].title").value(tasks.get(0).getTitle()))
                .andExpect(jsonPath("$[0].dueDate").value(tasks.get(0).getDueDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[1].dueDate").value(tasks.get(1).getDueDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }

    @Test
    void testUpdateTask() throws Exception {
        requestDto1
                .setTitle("Updated Task1")
                .setDescription("Updated Description1")
                .setDueDate(LocalDateTime.now().plusDays(1))
                .setStatus(Status.IN_PROGRESS);
        TaskResponseDto responseDto = new TaskResponseDto()
                .setStatus(requestDto1.getStatus());

        given(taskServiceMock.update(eq(requestDto1.getId()), any(TaskRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(put("/api/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(responseDto.getStatus().toString()));
    }

    @Test
    void testUpdateNonExistentTask() throws Exception {
        Long nonExistentId = 999L;
        given(taskServiceMock.update(eq(nonExistentId), any(TaskRequestDto.class)))
                .willThrow(new NotFoundException("Task with ID: " + nonExistentId + " not found"));

        mockMvc.perform(put("/api/tasks/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Task with ID: " + nonExistentId + " not found"));
    }

    @Test
    void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", requestDto1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(taskServiceMock).delete(requestDto1.getId());
    }

    @Test
    void testDeleteNonExistentTask() throws Exception {
        Long nonExistentId = 999L;

        willThrow(new NotFoundException("Task with ID: " + nonExistentId + " not found"))
                .given(taskServiceMock).delete(eq(nonExistentId));

        mockMvc.perform(delete("/api/tasks/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Task with ID: " + nonExistentId + " not found"));
    }
}