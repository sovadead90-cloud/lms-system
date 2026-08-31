package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class StudentRequestDto {
    @NotBlank(message = "Имя студента не должно быть пустым")
    private String firstName;

    @NotBlank(message = "Фамилия студента не должна быть пустой")
    private String lastName;

    @NotEmpty(message = "Студент должен быть записан минимум в одну группу")
    private Set<Long> groupIds; // Принимаем только ID групп
}
