package com.example.controller;

import com.example.dto.request.TeacherRequestDto;
import com.example.dto.response.TeacherResponseDto;
import com.example.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<TeacherResponseDto> createTeacher(@Valid @RequestBody TeacherRequestDto dto) {
        return new ResponseEntity<>(teacherService.createTeacher(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить данные преподавателя")
    public ResponseEntity<TeacherResponseDto> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherRequestDto dto) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить преподавателя")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о преподавателе по ID")
    public ResponseEntity<TeacherResponseDto> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @GetMapping
    @Operation(summary = "Просмотреть список преподавателей с фильтрами и пагинацией")
    public ResponseEntity<Page<TeacherResponseDto>> getAllTeachers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return ResponseEntity.ok(teacherService.searchTeachers(firstName, lastName, courseId, pageable));
    }
}
