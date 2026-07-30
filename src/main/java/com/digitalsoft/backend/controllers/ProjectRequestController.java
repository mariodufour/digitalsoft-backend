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
    public ResponseEntity<?> createRequest(@RequestBody NewProjectRequestDTO dto) {
        try {
            // Si todo sale bien, sigue funcionando exactamente igual que antes
            ProjectRequest createdRequest = projectRequestService.createProjectRequest(dto);
            return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);

        } catch (Exception e) {
            // Si algo falla, atrapamos el error y devolvemos el JSON amigable
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("error", "Tuvimos un inconveniente temporal al procesar tu solicitud. Por favor, intentá nuevamente más tarde."));
        }
    }
}