package com.example.service.impl;

import com.example.dto.request.TeacherRequestDto;
import com.example.dto.response.TeacherResponseDto;
import com.example.entity.Teacher;
import com.example.exception.ExceptionMessages;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.TeacherMapper;
import com.example.repository.TeacherRepository;
import com.example.service.TeacherService;
import com.example.specification.TeacherSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Override
    @Transactional
    public TeacherResponseDto createTeacher(TeacherRequestDto dto) {
        Teacher entity = teacherMapper.toEntity(dto);
        Teacher saved = teacherRepository.save(entity);
        return teacherMapper.toDto(saved);
    }

    @Override
    @Transactional
    public TeacherResponseDto updateTeacher(Long id, TeacherRequestDto dto) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.TEACHER_NOT_FOUND.formatted(id)));

        existing.setFirstName(dto.firstName());
        existing.setLastName(dto.lastName());

        return teacherMapper.toDto(teacherRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new ResourceNotFoundException(ExceptionMessages.TEACHER_NOT_FOUND.formatted(id));
        }
        teacherRepository.deleteById(id);
    }

    @Override
    public TeacherResponseDto getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .map(teacherMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.TEACHER_NOT_FOUND.formatted(id)));
    }

    @Override
    public Page<TeacherResponseDto> searchTeachers(String firstName, String lastName, Long courseId, Pageable pageable) {
        Specification<Teacher> spec = Specification
                .where(TeacherSpecifications.hasFirstName(firstName))
                .and(TeacherSpecifications.hasLastName(lastName))
                .and(TeacherSpecifications.teachesCourse(courseId));
        return teacherRepository.findAll(spec, pageable).map(teacherMapper::toDto);
    }
}
