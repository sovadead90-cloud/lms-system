package com.example.service.impl;

import com.example.dto.request.CourseRequestDto;
import com.example.dto.response.CourseResponseDto;
import com.example.entity.CourseEntity;
import com.example.entity.TeacherEntity;
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
        TeacherEntity teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Преподаватель с id " + dto.getTeacherId() + " не найден"));

        CourseEntity course = courseMapper.toEntity(dto);
        course.setTeacher(teacher); // Привязываем извлеченного преподавателя вручную

        return courseMapper.toDto(courseRepository.save(course));
    }

    @Override
    @Transactional
    public CourseResponseDto updateCourse(Long id, CourseRequestDto dto) {
        CourseEntity existing = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Курс с id " + id + " не найден"));

        TeacherEntity teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Преподаватель с id " + dto.getTeacherId() + " не найден"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setTeacher(teacher);

        return courseMapper.toDto(courseRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Курс с id " + id + " не найден");
        }
        courseRepository.deleteById(id);
    }

    @Override
    public CourseResponseDto getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Курс с id " + id + " не найден"));
    }

    @Override
    public Page<CourseResponseDto> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toDto);
    }
}