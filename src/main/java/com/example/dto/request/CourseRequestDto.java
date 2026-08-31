package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequestDto {
    @NotBlank(message = "Название курса не должно быть пустым")
    private String name;

    private String description;

    @NotNull(message = "Необходимо указать ID преподавателя для курса")
    private Long teacherId; // Связываем по ID, а не по объекту!
}
