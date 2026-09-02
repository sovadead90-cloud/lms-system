package com.example.mapper;

import com.example.dto.request.StudentRequestDto;
import com.example.dto.response.StudentResponseDto;
import com.example.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {GroupMapper.class})
public interface StudentMapper {

    @Mapping(target = "groups", ignore = true)
    Student toEntity(StudentRequestDto dto);

    StudentResponseDto toDto(Student entity);
}
