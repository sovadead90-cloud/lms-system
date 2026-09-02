package com.example.dto.response;

public record CourseResponseDto(
        Long id,
        String name,
        String description,
        TeacherResponseDto teacher
) {
}

