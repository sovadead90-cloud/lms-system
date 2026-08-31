package com.example.specification;

import com.example.entity.ScheduleEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class ScheduleSpecifications {

    private ScheduleSpecifications() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<ScheduleEntity> hasGroup(Long groupId) {
        return (root, query, cb) ->
                groupId == null ? cb.conjunction() :
                        cb.equal(root.get("group").get("id"), groupId);
    }

    public static Specification<ScheduleEntity> hasTeacher(Long teacherId) {
        return (root, query, cb) ->
                teacherId == null ? cb.conjunction() :
                        cb.equal(root.get("teacher").get("id"), teacherId);
    }

    public static Specification<ScheduleEntity> hasCourse(Long courseId) {
        return (root, query, cb) ->
                courseId == null ? cb.conjunction() :
                        cb.equal(root.get("course").get("id"), courseId);
    }

    public static Specification<ScheduleEntity> startTimeAfterOrEqual(LocalDateTime start) {
        return (root, query, cb) ->
                start == null ? cb.conjunction() :
                        cb.greaterThanOrEqualTo(root.get("startTime"), start);
    }

    public static Specification<ScheduleEntity> endTimeBeforeOrEqual(LocalDateTime end) {
        return (root, query, cb) ->
                end == null ? cb.conjunction() :
                        cb.lessThanOrEqualTo(root.get("endTime"), end);
    }
}
