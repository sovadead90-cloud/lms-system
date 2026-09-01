package com.example.specification;

import com.example.entity.Schedule;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class ScheduleSpecifications {

    private ScheduleSpecifications() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<Schedule> hasGroup(Long groupId) {
        return (root, query, cb) ->
                groupId == null ? cb.conjunction() :
                        cb.equal(root.get("group").get("id"), groupId);
    }

    public static Specification<Schedule> hasTeacher(Long teacherId) {
        return (root, query, cb) ->
                teacherId == null ? cb.conjunction() :
                        cb.equal(root.get("teacher").get("id"), teacherId);
    }

    public static Specification<Schedule> hasCourse(Long courseId) {
        return (root, query, cb) ->
                courseId == null ? cb.conjunction() :
                        cb.equal(root.get("course").get("id"), courseId);
    }

    public static Specification<Schedule> startTimeAfterOrEqual(LocalDateTime start) {
        return (root, query, cb) ->
                start == null ? cb.conjunction() :
                        cb.greaterThanOrEqualTo(root.get("startTime"), start);
    }

    public static Specification<Schedule> endTimeBeforeOrEqual(LocalDateTime end) {
        return (root, query, cb) ->
                end == null ? cb.conjunction() :
                        cb.lessThanOrEqualTo(root.get("endTime"), end);
    }
}
