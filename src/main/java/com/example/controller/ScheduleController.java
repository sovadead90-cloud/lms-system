package com.example.controller;

import com.example.dto.request.ScheduleRequestDto;
import com.example.dto.response.ScheduleResponseDto;
import com.example.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Расписание", description = "Управление временем проведения курсов для групп и просмотр графиков")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @Operation(summary = "Назначить время проведения курса для определённой группы")
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto dto) {
        return new ResponseEntity<>(scheduleService.createSchedule(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить время проведения курса для определённой группы")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequestDto dto) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить время проведения курса для определённой группы")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Универсальный поиск по расписанию (фильтр по группе, учителю, курсу и диапазону дат)")
    public ResponseEntity<Page<ScheduleResponseDto>> searchSchedules(
            @RequestParam(required = false) Long groupId,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) LocalDateTime startFrom,
            @RequestParam(required = false) LocalDateTime endTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        Page<ScheduleResponseDto> schedulePage = scheduleService.searchSchedules(
                groupId, teacherId, courseId, startFrom, endTo, pageable);
        return ResponseEntity.ok(schedulePage);
    }
}
