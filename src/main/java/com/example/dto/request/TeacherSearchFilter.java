package com.example.dto.request;

public record TeacherSearchFilter(
        String firstName,
        String lastName,
        Long courseId
) {
}

