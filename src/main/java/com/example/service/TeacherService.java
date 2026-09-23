package com.example.service;

import com.example.dto.request.TeacherRequestDto;
import com.example.dto.response.TeacherResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeacherService {
    TeacherResponseDto createTeacher(TeacherRequestDto dto);
    TeacherResponseDto updateTeacher(Long id, TeacherRequestDto dto);
    void deleteTeacher(Long id);
    TeacherResponseDto getTeacherById(Long id);
    Page<TeacherResponseDto> searchTeachers(String firstName, String lastName, Long courseId, Pageable pageable);
}