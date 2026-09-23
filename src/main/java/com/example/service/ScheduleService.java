package com.example.service;

import com.example.dto.request.ScheduleRequestDto;
import com.example.dto.response.ScheduleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ScheduleService {
    ScheduleResponseDto createSchedule(ScheduleRequestDto dto);
    ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto);
    void deleteSchedule(Long id);
    Page<ScheduleResponseDto> searchSchedules(Long groupId, Long teacherId, Long courseId,
                                              LocalDateTime startFrom, LocalDateTime endTo, Pageable pageable);
}