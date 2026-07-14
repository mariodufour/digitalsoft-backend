package com.digitalsoft.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_category_id", nullable = false)
    private ServiceCategory serviceCategory;

    @Column(name = "client_message", length = 2000, nullable = false)
    private String clientMessage; // "Contame qué necesitás"

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDIENTE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum RequestStatus {
        PENDIENTE,
        CONTACTADO,
        PRESUPUESTADO,
        RECHAZADO,
        FINALIZADO
    }
}