package com.example.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class StudentResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Set<GroupResponseDto> groups;
}
