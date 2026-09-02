package com.example.service;

import com.example.dto.request.StudentRequestDto;
import com.example.dto.response.StudentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    StudentResponseDto createStudent(StudentRequestDto dto);
    StudentResponseDto updateStudent(Long id, StudentRequestDto dto);
    void deleteStudent(Long id);
    StudentResponseDto getStudentById(Long id);
    Page<StudentResponseDto> searchStudents(String firstName, String lastName, Long groupId, Pageable pageable);
}
