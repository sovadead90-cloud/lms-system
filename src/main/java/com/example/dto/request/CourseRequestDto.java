package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseRequestDto(
        @NotBlank(message = "Название курса не должно быть пустым")
        String name,
        String description,
        @NotNull(message = "Необходимо указать ID преподавателя для курса")
        Long teacherId
) {
}
