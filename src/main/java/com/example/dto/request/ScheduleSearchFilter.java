package com.example.dto.request;

import java.time.LocalDateTime;

public record ScheduleSearchFilter(
        Long groupId,
        Long teacherId,
        Long courseId,
        LocalDateTime startFrom,
        LocalDateTime endTo
) {
}
