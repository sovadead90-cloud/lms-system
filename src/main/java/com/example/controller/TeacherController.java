package com.example.controller;

import com.example.dto.request.TeacherRequestDto;
import com.example.dto.request.TeacherSearchFilter;
import com.example.dto.response.TeacherResponseDto;
import com.example.service.TeacherService;
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
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
@Tag(name = "Преподаватели", description = "Управление преподавательским составом")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @Operation(summary = "Добавить нового преподавателя")
    public ResponseEntity<TeacherResponseDto> create(@Valid @RequestBody TeacherRequestDto dto) {
        return new ResponseEntity<>(teacherService.createTeacher(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить данные преподавателя")
    public ResponseEntity<TeacherResponseDto> update(@PathVariable Long id, @Valid @RequestBody TeacherRequestDto dto) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить преподавателя")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о преподавателе по ID")
    public ResponseEntity<TeacherResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @GetMapping
    @Operation(summary = "Просмотреть список преподавателей с фильтрами и пагинацией")
    public ResponseEntity<Page<TeacherResponseDto>> getAll(
            @ModelAttribute TeacherSearchFilter filter,
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable
    ) {
        Page<TeacherResponseDto> teachers = teacherService.searchTeachers(
                filter.firstName(),
                filter.lastName(),
                filter.courseId(),
                pageable
        );
        return ResponseEntity.ok(teachers);
    }
}
