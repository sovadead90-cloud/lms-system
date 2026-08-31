package com.example.service.impl;

import com.example.dto.request.GroupRequestDto;
import com.example.dto.response.GroupResponseDto;
import com.example.entity.GroupEntity;
import com.example.entity.StudentEntity;
import com.example.exception.BusinessRuleException;
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
        GroupEntity group = groupMapper.toEntity(dto);
        return groupMapper.toDto(groupRepository.save(group));
    }

    @Override
    @Transactional
    public GroupResponseDto updateGroup(Long id, GroupRequestDto dto) {
        GroupEntity existing = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + id + " не найдена"));
        existing.setName(dto.getName());
        return groupMapper.toDto(groupRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteGroup(Long id) {
        GroupEntity group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + id + " не найдена"));

        for (StudentEntity student : group.getStudents()) {
            if (student.getGroups().size() <= 1) {
                throw new BusinessRuleException("Нельзя удалить группу с id " + id +
                        ", так как студент " + student.getFirstName() + " " + student.getLastName() +
                        " больше не состоит ни в одной другой группе!");
            }
        }

        Set<StudentEntity> students = new HashSet<>(group.getStudents());
        for (StudentEntity student : students) {
            student.removeGroup(group);
        }

        groupRepository.delete(group);
    }

    @Override
    public GroupResponseDto getGroupById(Long id) {
        return groupRepository.findById(id)
                .map(groupMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + id + " не найдена"));
    }

    @Override
    public Page<GroupResponseDto> getAllGroups(Pageable pageable) {
        return groupRepository.findAll(pageable).map(groupMapper::toDto);
    }
}
