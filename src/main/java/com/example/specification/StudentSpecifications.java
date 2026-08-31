package com.example.specification;

import com.example.entity.GroupEntity;
import com.example.entity.StudentEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public final class StudentSpecifications {

    private StudentSpecifications() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Specification<StudentEntity> hasFirstName(String firstName) {
        return (root, query, cb) -> {
            if (firstName == null || firstName.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
        };
    }

    public static Specification<StudentEntity> hasLastName(String lastName) {
        return (root, query, cb) -> {
            if (lastName == null || lastName.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
        };
    }

    public static Specification<StudentEntity> isInGroup(Long groupId) {
        return (root, query, cb) -> {
            if (groupId == null) {
                return cb.conjunction();
            }
            Join<StudentEntity, GroupEntity> groupJoin = root.join("groups");
            return cb.equal(groupJoin.get("id"), groupId);
        };
    }
}
