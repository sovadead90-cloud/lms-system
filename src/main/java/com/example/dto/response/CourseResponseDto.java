package com.example.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponseDto {
    private Long id;
    private String name;
    private String description;
    private TeacherResponseDto teacher;
}
