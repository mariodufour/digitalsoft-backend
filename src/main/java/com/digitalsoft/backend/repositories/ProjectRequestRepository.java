package com.digitalsoft.backend.repositories;

import com.digitalsoft.backend.entities.ProjectRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRequestRepository extends JpaRepository<ProjectRequest, Long> {
    // Método para filtrar solicitudes por su estado (ej: buscar todos los PENDIENTES)
    List<ProjectRequest> findByStatus(ProjectRequest.RequestStatus status);
}