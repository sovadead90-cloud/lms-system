package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "student_group",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<GroupEntity> groups = new HashSet<>();


    public void addGroup(GroupEntity group) {
        this.groups.add(group);
        group.getStudents().add(this);
    }


    public void removeGroup(GroupEntity group) {
        this.groups.remove(group);
        group.getStudents().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentEntity studentEntity = (StudentEntity) o;
        return Objects.equals(id, studentEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
