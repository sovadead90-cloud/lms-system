package com.example.specification;

import com.example.entity.Course;
import com.example.entity.Teacher;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public final class TeacherSpecifications {

    private TeacherSpecifications() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<Teacher> hasFirstName(String firstName) {
        return (root, query, cb) ->
                (firstName == null || firstName.isBlank()) ? cb.conjunction() :
                        cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<Teacher> hasLastName(String lastName) {
        return (root, query, cb) ->
                (lastName == null || lastName.isBlank()) ? cb.conjunction() :
                        cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<Teacher> teachesCourse(Long courseId) {
        return (root, query, cb) -> {
            if (courseId == null) {
                return cb.conjunction();
            }
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Course> courseRoot = subquery.from(Course.class);
            subquery.select(courseRoot.get("teacher").get("id"))
                    .where(cb.equal(courseRoot.get("id"), courseId));
            return cb.in(root.get("id")).value(subquery);
        };
    }
}
