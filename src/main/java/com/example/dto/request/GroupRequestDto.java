package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GroupRequestDto(
        @NotBlank(message = "Название группы не должно быть пустым")
        String name
) {
}

