package com.example.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScheduleRequestDto(
        @NotNull(message = "Необходимо указать ID группы")
        Long groupId,

        @NotNull(message = "Необходимо указать ID преподавателя")
        Long teacherId,

        @NotNull(message = "Необходимо указать ID курса")
        Long courseId,

        @NotNull(message = "Дата начала занятия обязательна")
        LocalDateTime startTime,

        @NotNull(message = "Дата окончания занятия обязательна")
        LocalDateTime endTime
) {
}
