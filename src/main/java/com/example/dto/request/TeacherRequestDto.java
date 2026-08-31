package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherRequestDto {
    @NotBlank(message = "Имя преподавателя не должно быть пустым")
    private String firstName;

    @NotBlank(message = "Фамилия преподавателя не должна быть пустой")
    private String lastName;
}
