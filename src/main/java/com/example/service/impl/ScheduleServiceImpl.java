package com.example.service.impl;

import com.example.dto.request.ScheduleRequestDto;
import com.example.dto.response.ScheduleResponseDto;
import com.example.entity.CourseEntity;
import com.example.entity.GroupEntity;
import com.example.entity.ScheduleEntity;
import com.example.entity.TeacherEntity;
import com.example.exception.BusinessRuleException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.ScheduleMapper;
import com.example.repository.CourseRepository;
import com.example.repository.GroupRepository;
import com.example.repository.ScheduleRepository;
import com.example.repository.TeacherRepository;
import com.example.service.ScheduleService;
import com.example.specification.ScheduleSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto dto) {
        GroupEntity group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + dto.getGroupId() + " не найдена"));

        TeacherEntity teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Преподаватель с id " + dto.getTeacherId() + " не найден"));

        CourseEntity course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Курс с id " + dto.getCourseId() + " не найден"));

        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new BusinessRuleException("Преподаватель " + teacher.getFirstName() + " " + teacher.getLastName() +
                    " не может вести курс '" + course.getName() + "', так как за этим курсом закреплен другой учитель!");
        }

        ScheduleEntity schedule = scheduleMapper.toEntity(dto);
        schedule.setGroup(group);
        schedule.setTeacher(teacher);
        schedule.setCourse(course);

        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    @Override
    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto) {
        ScheduleEntity existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запись расписания с id " + id + " не найдена"));

        GroupEntity group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + dto.getGroupId() + " не найдена"));

        TeacherEntity teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Преподаватель с id " + dto.getTeacherId() + " не найден"));

        CourseEntity course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Курс с id " + dto.getCourseId() + " не найден"));

        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new BusinessRuleException("Преподаватель не соответствует данному курсу!");
        }

        existing.setGroup(group);
        existing.setTeacher(teacher);
        existing.setCourse(course);
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());

        return scheduleMapper.toDto(scheduleRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Запись расписания с id " + id + " не найдена");
        }
        scheduleRepository.deleteById(id);
    }

    @Override
    public Page<ScheduleResponseDto> searchSchedules(Long groupId, Long teacherId, Long courseId,
                                                     LocalDateTime startFrom, LocalDateTime endTo, Pageable pageable) {
        Specification<ScheduleEntity> spec = Specification
                .where(ScheduleSpecifications.hasGroup(groupId))
                .and(ScheduleSpecifications.hasTeacher(teacherId))
                .and(ScheduleSpecifications.hasCourse(courseId))
                .and(ScheduleSpecifications.startTimeAfterOrEqual(startFrom))
                .and(ScheduleSpecifications.endTimeBeforeOrEqual(endTo));
        return scheduleRepository.findAll(spec, pageable).map(scheduleMapper::toDto);
    }
}
