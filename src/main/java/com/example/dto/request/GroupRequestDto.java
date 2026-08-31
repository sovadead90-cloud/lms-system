package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupRequestDto {
    @NotBlank(message = "Название группы не должно быть пустым")
    private String name;
}
