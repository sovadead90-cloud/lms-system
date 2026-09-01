package com.example.service.impl;

import com.example.dto.request.CourseRequestDto;
import com.example.dto.response.CourseResponseDto;
import com.example.entity.Course;
import com.example.entity.Teacher;
import com.example.exception.ExceptionMessages;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.CourseMapper;
import com.example.repository.CourseRepository;
import com.example.repository.TeacherRepository;
import com.example.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseResponseDto createCourse(CourseRequestDto dto) {
        Teacher teacher = teacherRepository.findById(dto.teacherId())
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.TEACHER_NOT_FOUND.formatted(dto.teacherId())));

        Course course = courseMapper.toEntity(dto);
        course.setTeacher(teacher);

        return courseMapper.toDto(courseRepository.save(course));
    }

    @Override
    @Transactional
    public CourseResponseDto updateCourse(Long id, CourseRequestDto dto) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.COURSE_NOT_FOUND.formatted(id)));

        Teacher teacher = teacherRepository.findById(dto.teacherId())
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.TEACHER_NOT_FOUND.formatted(dto.teacherId())));

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setTeacher(teacher);

        return courseMapper.toDto(courseRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException(ExceptionMessages.COURSE_NOT_FOUND.formatted(id));
        }
        courseRepository.deleteById(id);
    }

    @Override
    public CourseResponseDto getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.COURSE_NOT_FOUND.formatted(id)));
    }

    @Override
    public Page<CourseResponseDto> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toDto);
    }
}