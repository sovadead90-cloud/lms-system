package com.example.service.impl;

import com.example.dto.request.StudentRequestDto;
import com.example.dto.response.StudentResponseDto;
import com.example.entity.Group;
import com.example.entity.Student;
import com.example.exception.BusinessRuleException;
import com.example.exception.ExceptionMessages;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.StudentMapper;
import com.example.repository.GroupRepository;
import com.example.repository.StudentRepository;
import com.example.service.StudentService;
import com.example.specification.StudentSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public StudentResponseDto createStudent(StudentRequestDto dto) {
        if (dto.groupIds() == null || dto.groupIds().isEmpty()) {
            throw new BusinessRuleException("Студент должен быть записан минимум в одну группу!");
        }

        Student student = studentMapper.toEntity(dto);

        for (Long groupId : dto.groupIds()) {
            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.GROUP_NOT_FOUND.formatted(groupId)));
            student.addGroup(group);
        }

        Student saved = studentRepository.save(student);
        return studentMapper.toDto(saved);
    }

    @Override
    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto dto) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.STUDENT_NOT_FOUND.formatted(id)));

        if (dto.groupIds() == null || dto.groupIds().isEmpty()) {
            throw new BusinessRuleException("Студент должен состоять минимум в одной группе!");
        }

        existing.setFirstName(dto.firstName());
        existing.setLastName(dto.firstName());

        Set<Group> oldGroups = new HashSet<>(existing.getGroups());
        for (Group group : oldGroups) {
            existing.removeGroup(group);
        }

        for (Long groupId : dto.groupIds()) {
            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.GROUP_NOT_FOUND.formatted(groupId)));
            existing.addGroup(group);
        }

        return studentMapper.toDto(studentRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.STUDENT_NOT_FOUND.formatted(id)));

        Set<Group> groups = new HashSet<>(student.getGroups());
        for (Group group : groups) {
            student.removeGroup(group);
        }

        studentRepository.delete(student);
    }

    @Override
    public StudentResponseDto getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.STUDENT_NOT_FOUND.formatted(id)));
    }

    public Page<StudentResponseDto> searchStudents(String firstName, String lastName, Long groupId, Pageable pageable) {
        Specification<Student> spec = Specification
                .where(StudentSpecifications.hasFirstName(firstName))
                .and(StudentSpecifications.hasLastName(lastName))
                .and(StudentSpecifications.isInGroup(groupId));
        Page<Student> studentPage = studentRepository.findAll(spec, pageable);
        return studentPage.map(studentMapper::toDto);
    }
}
