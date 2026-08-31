package com.example.mapper;

import com.example.dto.request.StudentRequestDto;
import com.example.dto.response.StudentResponseDto;
import com.example.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {GroupMapper.class})
public interface StudentMapper {

    @Mapping(target = "groups", ignore = true)
    StudentEntity toEntity(StudentRequestDto dto);

    StudentResponseDto toDto(StudentEntity entity);
}
