package com.example.controller;

import com.example.dto.request.GroupRequestDto;
import com.example.dto.response.GroupResponseDto;
import com.example.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Tag(name = "Группы", description = "Управление учебными группами")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @Operation(summary = "Создать новую группу")
    public ResponseEntity<GroupResponseDto> createGroup(@Valid @RequestBody GroupRequestDto dto) {
        return new ResponseEntity<>(groupService.createGroup(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить название группы")
    public ResponseEntity<GroupResponseDto> updateGroup(@PathVariable Long id, @Valid @RequestBody GroupRequestDto dto) {
        return ResponseEntity.ok(groupService.updateGroup(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить группу", description = "Удаление запрещено, если в группе останутся студенты без других групп")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о группе по ID")
    public ResponseEntity<GroupResponseDto> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @GetMapping
    @Operation(summary = "Просмотреть список всех групп с пагинацией")
    public ResponseEntity<Page<GroupResponseDto>> getAllGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return ResponseEntity.ok(groupService.getAllGroups(pageable));
    }
}