package com.example.controller;

import com.example.AbstractIT;
import com.example.dto.ErrorResponse;
import com.example.dto.request.ScheduleRequestDto;
import com.example.dto.response.ScheduleResponseDto;
import com.example.entity.Course;
import com.example.entity.Group;
import com.example.entity.Teacher;
import com.example.entity.Schedule;
import com.example.repository.CourseRepository;
import com.example.repository.GroupRepository;
import com.example.repository.ScheduleRepository;
import com.example.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleControllerIT extends AbstractIT {

    private static final String BASE_URL = "/api/v1/schedules";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CourseRepository courseRepository;

    private Teacher testTeacher;
    private Group testGroup;
    private Course testCourse;
    private Schedule testSchedule;

    @BeforeEach
    void setUp() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Алексей");
        teacher.setLastName("Петров");
        testTeacher = teacherRepository.save(teacher);

        Group group = new Group();
        group.setName("Java-Core-21");
        testGroup = groupRepository.save(group);

        Course course = new Course();
        course.setName("Spring Boot");
        course.setDescription("Интенсив");
        course.setTeacher(testTeacher);
        testCourse = courseRepository.save(course);

        Schedule schedule = new Schedule();
        schedule.setGroup(testGroup);
        schedule.setTeacher(testTeacher);
        schedule.setCourse(testCourse);
        schedule.setStartTime(LocalDateTime.of(2026, 9, 1, 9, 0));
        schedule.setEndTime(LocalDateTime.of(2026, 9, 1, 12, 0));
        testSchedule = scheduleRepository.save(schedule);
    }

    @AfterEach
    void tearDown() {
        scheduleRepository.deleteAll();
        courseRepository.deleteAll();
        groupRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    void shouldCreateScheduleSuccessfully() {
        ScheduleRequestDto request = new ScheduleRequestDto(
                testGroup.getId(),
                testTeacher.getId(),
                testCourse.getId(),
                LocalDateTime.of(2026, 9, 2, 14, 0),
                LocalDateTime.of(2026, 9, 2, 17, 0)
        );

        ResponseEntity<ScheduleResponseDto> response = restTemplate.postForEntity(
                BASE_URL,
                request,
                ScheduleResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().group().name()).isEqualTo("Java-Core-21");

        assertThat(scheduleRepository.count()).isEqualTo(2);
    }

    @Test
    void shouldDeleteScheduleSuccessfully() {
        ResponseEntity<Void> response = restTemplate.exchange(
                BASE_URL + "/" + testSchedule.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(scheduleRepository.existsById(testSchedule.getId())).isFalse();
    }

    @Test
    void shouldSearchSchedulesByFilterWithModelAttribute() {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/search")
                .queryParam("groupId", testGroup.getId())
                .toUriString();

        ParameterizedTypeReference<RestResponsePage<ScheduleResponseDto>> responseType =
                new ParameterizedTypeReference<>() {
                };

        ResponseEntity<RestResponsePage<ScheduleResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                responseType
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).isNotEmpty();
        assertThat(response.getBody().getContent().get(0).group().id()).isEqualTo(testGroup.getId());
    }

    @Test
    void shouldReturnBadRequestWhenTeacherDoesNotConductCourse() {
        Teacher wrongTeacher = new Teacher();
        wrongTeacher.setFirstName("Петр");
        wrongTeacher.setLastName("Сидоров");
        wrongTeacher = teacherRepository.save(wrongTeacher);

        ScheduleRequestDto request = new ScheduleRequestDto(
                testGroup.getId(),
                wrongTeacher.getId(), // Передаем некорректного учителя
                testCourse.getId(),
                LocalDateTime.of(2026, 9, 3, 10, 0),
                LocalDateTime.of(2026, 9, 3, 13, 0)
        );

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                BASE_URL, request, ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isEqualTo("Business Rule Violation");
        assertThat(scheduleRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldUpdateScheduleTimeSuccessfully() {
        ScheduleRequestDto updateRequest = new ScheduleRequestDto(
                testGroup.getId(),
                testTeacher.getId(),
                testCourse.getId(),
                LocalDateTime.of(2026, 9, 10, 15, 0), // Переносим занятие на 10 сентября
                LocalDateTime.of(2026, 9, 10, 18, 0)
        );

        ResponseEntity<ScheduleResponseDto> response = restTemplate.exchange(
                BASE_URL + "/" + testSchedule.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                ScheduleResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().startTime()).isEqualTo("2026-09-10T15:00:00");

        Schedule updatedEntity = scheduleRepository.findById(testSchedule.getId()).orElseThrow();
        assertThat(updatedEntity.getStartTime()).isEqualTo(LocalDateTime.of(2026, 9, 10, 15, 0));
    }

    @Test
    void shouldReturnBadRequestWhenStartTimeIsNull() {
        ScheduleRequestDto invalidRequest = new ScheduleRequestDto(
                testGroup.getId(),
                testTeacher.getId(),
                testCourse.getId(),
                null,
                LocalDateTime.of(2026, 9, 2, 17, 0)
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
                BASE_URL, invalidRequest, Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("startTime");
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentSchedule() {
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                BASE_URL + "/99999",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().error()).isEqualTo("Resource Not Found");
    }

    @Test
    void shouldReturnEmptyPageWhenNoSchedulesMatchFilter() {
        String url = UriComponentsBuilder.fromUriString( BASE_URL + "/search")
                .queryParam("groupId", 99999L)
                .toUriString();

        ParameterizedTypeReference<RestResponsePage<ScheduleResponseDto>> responseType =
                new ParameterizedTypeReference<>() {
                };

        ResponseEntity<RestResponsePage<ScheduleResponseDto>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, responseType
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).isEmpty();
    }
}