package com.example.mapper;

import com.example.dto.request.ScheduleRequestDto;
import com.example.dto.response.ScheduleResponseDto;
import com.example.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {GroupMapper.class, TeacherMapper.class, CourseMapper.class})
public interface ScheduleMapper {

    @Mapping(target = "group", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "course", ignore = true)
    Schedule toEntity(ScheduleRequestDto dto);

    ScheduleResponseDto toDto(Schedule entity);
}
