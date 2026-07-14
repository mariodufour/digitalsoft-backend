package com.digitalsoft.backend.controllers;

import com.digitalsoft.backend.dtos.NewProjectRequestDTO;
import com.digitalsoft.backend.entities.ProjectRequest;
import com.digitalsoft.backend.services.ProjectRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProjectRequestController {

    private final ProjectRequestService projectRequestService;

    @PostMapping
    public ResponseEntity<ProjectRequest> createRequest(@RequestBody NewProjectRequestDTO dto) {
        ProjectRequest createdRequest = projectRequestService.createProjectRequest(dto);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }
}