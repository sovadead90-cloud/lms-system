package com.example.controller;

import com.example.dto.request.ScheduleSearchFilter;
import com.example.dto.request.ScheduleRequestDto;
import com.example.dto.response.ScheduleResponseDto;
import com.example.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Расписание", description = "Управление временем проведения курсов для групп и просмотр графиков")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @Operation(summary = "Назначить время проведения курса для определённой группы")
    public ResponseEntity<ScheduleResponseDto> create(@Valid @RequestBody ScheduleRequestDto dto) {
        return new ResponseEntity<>(scheduleService.createSchedule(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить время проведения курса для определённой группы")
    public ResponseEntity<ScheduleResponseDto> update(@PathVariable Long id, @Valid @RequestBody ScheduleRequestDto dto) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить время проведения курса для определённой группы")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Универсальный поиск по расписанию (фильтр по группе, учителю, курсу и диапазону дат)")
    public ResponseEntity<Page<ScheduleResponseDto>> search(
            @ModelAttribute ScheduleSearchFilter filter,
            @PageableDefault(page = 0, size = 10, sort = "startTime") Pageable pageable
    ) {
        Page<ScheduleResponseDto> schedulePage = scheduleService.searchSchedules(
                filter.groupId(),
                filter.teacherId(),
                filter.courseId(),
                filter.startFrom(),
                filter.endTo(),
                pageable
        );
        return ResponseEntity.ok(schedulePage);
    }
}
