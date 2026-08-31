package com.example.mapper;

import com.example.dto.request.CourseRequestDto;
import com.example.dto.response.CourseResponseDto;
import com.example.entity.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class})
public interface CourseMapper {

    @Mapping(target = "teacher", ignore = true)
    CourseEntity toEntity(CourseRequestDto dto);

    CourseResponseDto toDto(CourseEntity entity);
}
