package com.example.openschool2.dto;

import com.example.openschool2.model.Status;
import com.example.openschool2.util.LocalDateTimeDeserializer;
import com.example.openschool2.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dueDate;

    private Status status;
}
