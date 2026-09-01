package com.example.service.impl;

import com.example.dto.request.GroupRequestDto;
import com.example.dto.response.GroupResponseDto;
import com.example.entity.Group;
import com.example.entity.Student;
import com.example.exception.BusinessRuleException;
import com.example.exception.ExceptionMessages;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.GroupMapper;
import com.example.repository.GroupRepository;
import com.example.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    @Override
    @Transactional
    public GroupResponseDto createGroup(GroupRequestDto dto) {
        Group group = groupMapper.toEntity(dto);
        return groupMapper.toDto(groupRepository.save(group));
    }

    @Override
    @Transactional
    public GroupResponseDto updateGroup(Long id, GroupRequestDto dto) {
        Group existing = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.GROUP_NOT_FOUND.formatted(id)));
        existing.setName(dto.name());
        return groupMapper.toDto(groupRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.GROUP_NOT_FOUND.formatted(id)));

        for (Student student : group.getStudents()) {
            if (student.getGroups().size() <= 1) {
                throw new BusinessRuleException("Нельзя удалить группу с id " + id +
                        ", так как студент " + student.getFirstName() + " " + student.getLastName() +
                        " больше не состоит ни в одной другой группе!");
            }
        }

        Set<Student> students = new HashSet<>(group.getStudents());
        for (Student student : students) {
            student.removeGroup(group);
        }

        groupRepository.delete(group);
    }

    @Override
    public GroupResponseDto getGroupById(Long id) {
        return groupRepository.findById(id)
                .map(groupMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.GROUP_NOT_FOUND.formatted(id)));
    }

    @Override
    public Page<GroupResponseDto> getAllGroups(Pageable pageable) {
        return groupRepository.findAll(pageable).map(groupMapper::toDto);
    }
}
