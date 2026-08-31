package com.example.mapper;

import com.example.dto.request.ScheduleRequestDto;
import com.example.dto.response.ScheduleResponseDto;
import com.example.entity.ScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {GroupMapper.class, TeacherMapper.class, CourseMapper.class})
public interface ScheduleMapper {

    @Mapping(target = "group", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "course", ignore = true)
    ScheduleEntity toEntity(ScheduleRequestDto dto);

    ScheduleResponseDto toDto(ScheduleEntity entity);
}
