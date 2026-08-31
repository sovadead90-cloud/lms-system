package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleRequestDto {
    @NotNull(message = "Необходимо указать ID группы")
    private Long groupId;

    @NotNull(message = "Необходимо указать ID преподавателя")
    private Long teacherId;

    @NotNull(message = "Необходимо указать ID курса")
    private Long courseId;

    @NotNull(message = "Дата начала занятия обязательна")
    private LocalDateTime startTime;

    @NotNull(message = "Дата окончания занятия обязательна")
    private LocalDateTime endTime;
}
