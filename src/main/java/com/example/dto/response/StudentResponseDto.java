package com.example.dto.response;

import java.util.Set;

public record StudentResponseDto(
        Long id,
        String firstName,
        String lastName,
        Set<GroupResponseDto> groups
) {
}

