package com.example.mapper;

import com.example.dto.request.TeacherRequestDto;
import com.example.dto.response.TeacherResponseDto;
import com.example.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeacherMapper {
    Teacher toEntity(TeacherRequestDto dto);
    TeacherResponseDto toDto(Teacher entity);
}
