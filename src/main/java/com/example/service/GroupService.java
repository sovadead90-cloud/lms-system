package com.example.service;

import com.example.dto.request.GroupRequestDto;
import com.example.dto.response.GroupResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {
    GroupResponseDto createGroup(GroupRequestDto dto);
    GroupResponseDto updateGroup(Long id, GroupRequestDto dto);
    void deleteGroup(Long id);
    GroupResponseDto getGroupById(Long id);
    Page<GroupResponseDto> getAllGroups(Pageable pageable);
}
