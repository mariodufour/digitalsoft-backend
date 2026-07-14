package com.digitalsoft.backend.repositories;

import com.digitalsoft.backend.entities.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    // Un método personalizado útil para buscar una categoría por su nombre exacto
    Optional<ServiceCategory> findByName(String name);
}