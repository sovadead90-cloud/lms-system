package com.example.service.impl;

import com.example.dto.request.StudentRequestDto;
import com.example.dto.response.StudentResponseDto;
import com.example.entity.GroupEntity;
import com.example.entity.StudentEntity;
import com.example.exception.BusinessRuleException;
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
        if (dto.getGroupIds() == null || dto.getGroupIds().isEmpty()) {
            throw new BusinessRuleException("Студент должен быть записан минимум в одну группу!");
        }

        StudentEntity student = studentMapper.toEntity(dto);

        for (Long groupId : dto.getGroupIds()) {
            GroupEntity group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + groupId + " не найдена"));
            student.addGroup(group);
        }

        StudentEntity saved = studentRepository.save(student);
        return studentMapper.toDto(saved);
    }

    @Override
    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto dto) {
        StudentEntity existing = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Студент с id " + id + " не найден"));

        if (dto.getGroupIds() == null || dto.getGroupIds().isEmpty()) {
            throw new BusinessRuleException("Студент должен состоять минимум в одной группе!");
        }

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());

        Set<GroupEntity> oldGroups = new HashSet<>(existing.getGroups());
        for (GroupEntity group : oldGroups) {
            existing.removeGroup(group);
        }

        for (Long groupId : dto.getGroupIds()) {
            GroupEntity group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + groupId + " не найдена"));
            existing.addGroup(group);
        }

        return studentMapper.toDto(studentRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Студент с id " + id + " не найден"));

        Set<GroupEntity> groups = new HashSet<>(student.getGroups());
        for (GroupEntity group : groups) {
            student.removeGroup(group);
        }

        studentRepository.delete(student);
    }

    @Override
    public StudentResponseDto getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Студент с id " + id + " не найден"));
    }

    public Page<StudentResponseDto> searchStudents(String firstName, String lastName, Long groupId, Pageable pageable) {
        Specification<StudentEntity> spec = Specification
                .where(StudentSpecifications.hasFirstName(firstName))
                .and(StudentSpecifications.hasLastName(lastName))
                .and(StudentSpecifications.isInGroup(groupId));
        Page<StudentEntity> studentPage = studentRepository.findAll(spec, pageable);
        return studentPage.map(studentMapper::toDto);
    }
}
