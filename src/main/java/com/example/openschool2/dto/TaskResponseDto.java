package com.example.openschool2.dto;

import com.example.openschool2.model.Status;
import com.example.openschool2.util.LocalDateTimeDeserializer;
import com.example.openschool2.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TaskResponseDto {
    private Long id;

    private String title;

    private String description;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dueDate;

    private Status status;
}
