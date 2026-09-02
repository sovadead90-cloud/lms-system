package com.example.mapper;

import com.example.dto.request.CourseRequestDto;
import com.example.dto.response.CourseResponseDto;
import com.example.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {TeacherMapper.class})
public interface CourseMapper {

    @Mapping(target = "teacher", ignore = true)
    Course toEntity(CourseRequestDto dto);

    CourseResponseDto toDto(Course entity);
}
