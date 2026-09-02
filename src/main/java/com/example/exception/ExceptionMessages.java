package com.example.exception;

public final class ExceptionMessages {

    private ExceptionMessages() {
        throw new UnsupportedOperationException("Utility class for exception messages");
    }

    public static final String TEACHER_NOT_FOUND = "Преподаватель с id %d не найден";
    public static final String STUDENT_NOT_FOUND = "Студент с id %d не найден";
    public static final String GROUP_NOT_FOUND = "Группа с id %d не найдена";
    public static final String COURSE_NOT_FOUND = "Курс с id %d не найден";
    public static final String SCHEDULE_NOT_FOUND = "Запись расписания с id %d не найдена";
}
