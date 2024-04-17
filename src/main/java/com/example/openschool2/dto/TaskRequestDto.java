package com.example.openschool2.dto;

import com.example.openschool2.model.Status;
import com.example.openschool2.util.LocalDateTimeDeserializer;
import com.example.openschool2.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Title must not be empty")
    private String title;

    @NotNull(message = "Description must not be null")
    private String description;

    @FutureOrPresent(message = "Due date must be in the future")
    @NotNull(message = "Due Date cannot be empty")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dueDate;
    @NotNull(message = "Status must not be null")
    private Status status;
}
