package com.example.mapper;

import com.example.dto.request.GroupRequestDto;
import com.example.dto.response.GroupResponseDto;
import com.example.entity.GroupEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupEntity toEntity(GroupRequestDto dto);
    GroupResponseDto toDto(GroupEntity entity);
}
