package com.example.dto.response;

import java.time.LocalDateTime;

public record ScheduleResponseDto(
        Long id,
        GroupResponseDto group,
        TeacherResponseDto teacher,
        CourseResponseDto course,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}

