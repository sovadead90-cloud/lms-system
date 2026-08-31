package com.example.controller;

import com.example.dto.request.StudentRequestDto;
import com.example.dto.response.StudentResponseDto;
import com.example.service.StudentService;
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
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Tag(name = "Студенты", description = "Управление студентами и их группами")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Добавить нового студента", description = "Студент обязательно должен содержать минимум одну существующую группу")
    public ResponseEntity<StudentResponseDto> createStudent(@Valid @RequestBody StudentRequestDto dto) {
        return new ResponseEntity<>(studentService.createStudent(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить данные студента")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequestDto dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить студента")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о студенте по ID")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping
    @Operation(
            summary = "Просмотреть список всех студентов с динамической фильтрацией и пагинацией",
            description = "Позволяет искать студентов по имени, фамилии и группе. Любой фильтр можно оставить пустым."
    )
    public ResponseEntity<Page<StudentResponseDto>> getAllStudents(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Long groupId,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<StudentResponseDto> students = studentService.searchStudents(firstName, lastName, groupId, pageable);
        return ResponseEntity.ok(students);
    }
}