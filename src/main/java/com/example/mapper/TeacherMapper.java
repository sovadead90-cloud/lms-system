package com.example.mapper;

import com.example.dto.request.TeacherRequestDto;
import com.example.dto.response.TeacherResponseDto;
import com.example.entity.TeacherEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    TeacherEntity toEntity(TeacherRequestDto dto);
    TeacherResponseDto toDto(TeacherEntity entity);
}
