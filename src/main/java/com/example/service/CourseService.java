package com.example.service;

import com.example.dto.request.CourseRequestDto;
import com.example.dto.response.CourseResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    CourseResponseDto createCourse(CourseRequestDto dto);
    CourseResponseDto updateCourse(Long id, CourseRequestDto dto);
    void deleteCourse(Long id);
    CourseResponseDto getCourseById(Long id);
    Page<CourseResponseDto> getAllCourses(Pageable pageable);
}
