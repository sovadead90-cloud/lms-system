CREATE TABLE teachers
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL
);

CREATE TABLE courses
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    teacher_id  BIGINT       NOT NULL,
    CONSTRAINT fk_courses_teacher FOREIGN KEY (teacher_id) REFERENCES teachers (id)
);

CREATE TABLE groups
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE students
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL
);

CREATE TABLE student_group
(
    student_id BIGINT NOT NULL,
    group_id   BIGINT NOT NULL,
    PRIMARY KEY (student_id, group_id),
    CONSTRAINT fk_sg_student FOREIGN KEY (student_id) REFERENCES students (id),
    CONSTRAINT fk_sg_group FOREIGN KEY (group_id) REFERENCES groups (id)
);

CREATE TABLE schedules
(
    id         BIGSERIAL PRIMARY KEY,
    group_id   BIGINT    NOT NULL,
    teacher_id BIGINT    NOT NULL,
    course_id  BIGINT    NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time   TIMESTAMP NOT NULL,
    CONSTRAINT fk_schedules_group FOREIGN KEY (group_id) REFERENCES groups (id),
    CONSTRAINT fk_schedules_teacher FOREIGN KEY (teacher_id) REFERENCES teachers (id),
    CONSTRAINT fk_schedules_course FOREIGN KEY (course_id) REFERENCES courses (id)
);

CREATE INDEX idx_schedule_end_time ON schedules (end_time);