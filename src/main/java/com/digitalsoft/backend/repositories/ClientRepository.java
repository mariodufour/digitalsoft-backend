package com.digitalsoft.backend.repositories;

import com.digitalsoft.backend.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Verificar si un cliente ya existe por su teléfono (WhatsApp) antes de duplicarlo
    Optional<Client> findByPhone(String phone);
}