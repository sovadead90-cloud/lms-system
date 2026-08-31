package com.example.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleResponseDto {
    private Long id;
    private GroupResponseDto group;
    private TeacherResponseDto teacher;
    private CourseResponseDto course;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
