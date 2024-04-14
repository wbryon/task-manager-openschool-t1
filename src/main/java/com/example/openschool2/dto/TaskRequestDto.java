package com.example.openschool2.dto;

import com.example.openschool2.model.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class TaskRequestDto {
    private Long id;

    private String title;

    private String description;

    @FutureOrPresent(message = "Due date must be in the future")
    @NotNull(message = "Due Date cannot be null")
    private LocalDateTime dueDate;

    private Status status;
}
