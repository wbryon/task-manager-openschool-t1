package com.example.openschool2.dto;

import com.example.openschool2.model.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TaskResponseDto {
    private Long id;

    private String title;

    private String description;

    private LocalDateTime dueDate;

    private Status status;
}
