package com.example.dto.request;

public record StudentSearchFilter(
        String firstName,
        String lastName,
        Long groupId
) {
}

