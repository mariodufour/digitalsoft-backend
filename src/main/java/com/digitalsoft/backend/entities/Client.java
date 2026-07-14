package com.digitalsoft.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_name")
    private String businessName; // Nombre del emprendimiento

    @Column(name = "contact_name", nullable = false)
    private String contactName; // Nombre de la persona

    @Column(nullable = false)
    private String phone; // Clave para el contacto por WhatsApp

    private String email;
}