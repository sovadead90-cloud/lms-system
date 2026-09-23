package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record StudentRequestDto(
        @NotBlank(message = "Имя студента не должно быть пустым")
        String firstName,

        @NotBlank(message = "Фамилия студента не должна быть пустой")
        String lastName,

        @NotEmpty(message = "Студент должен быть записан минимум в одну группу")
        Set<Long> groupIds
) {
}

