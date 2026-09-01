package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TeacherRequestDto(
        @NotBlank(message = "Имя преподавателя не должно быть пустым")
        String firstName,

        @NotBlank(message = "Фамилия преподавателя не должна быть пустой")
        String lastName
) {
}

