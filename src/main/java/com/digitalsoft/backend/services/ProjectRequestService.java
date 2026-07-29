package com.digitalsoft.backend.services;

import com.digitalsoft.backend.dtos.NewProjectRequestDTO;
import com.digitalsoft.backend.entities.Client;
import com.digitalsoft.backend.entities.ProjectRequest;
import com.digitalsoft.backend.entities.ServiceCategory;
import com.digitalsoft.backend.repositories.ClientRepository;
import com.digitalsoft.backend.repositories.ProjectRequestRepository;
import com.digitalsoft.backend.repositories.ServiceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectRequestService {

    private final ProjectRequestRepository projectRequestRepository;
    private final ClientRepository clientRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    @Transactional
    public ProjectRequest createProjectRequest(NewProjectRequestDTO dto) {
        // 1. Validar que la categoría de servicio realmente exista
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(dto.getServiceCategoryId())
                .orElseThrow(() -> new RuntimeException("La categoría de servicio no existe con el ID: " + dto.getServiceCategoryId()));

        // 2. Buscar si el cliente ya existe por su número de teléfono (WhatsApp)
        Client client = clientRepository.findByPhone(dto.getPhone())
                .orElseGet(() -> {
                    // Si no existe, creamos un cliente nuevo
                    Client newClient = new Client();
                    newClient.setBusinessName(dto.getBusinessName());
                    newClient.setContactName(dto.getContactName());
                    newClient.setPhone(dto.getPhone());
                    newClient.setEmail(dto.getEmail());
                    return clientRepository.save(newClient);
                });

        // 3. Crear y mapear la nueva solicitud de proyecto
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setClient(client);
        projectRequest.setServiceCategory(serviceCategory);
        projectRequest.setClientMessage(dto.getClientMessage());

        // 4. Guardar en la base de datos MySQL
        ProjectRequest savedRequest = projectRequestRepository.save(projectRequest);

        // 5. Disparar la notificación por correo al guardarlo con éxito
        enviarNotificacion(dto);

        return savedRequest;
    }

    private void enviarNotificacion(NewProjectRequestDTO dto) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.brevo.com/v3/smtp/email";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);
            headers.set("accept", "application/json");

            // 1. Armamos el texto exacto que ya tenías
            String texto = "¡Recibiste una nueva solicitud de desarrollo!\n\n"
                    + "👤 Cliente: " + dto.getContactName() + "\n"
                    + "🏢 Negocio: " + dto.getBusinessName() + "\n"
                    + "📱 Teléfono: " + dto.getPhone() + "\n"
                    + "✉️ Email: " + (dto.getEmail() != null && !dto.getEmail().isEmpty() ? dto.getEmail() : "No especificado") + "\n\n"
                    + "💬 Mensaje del cliente:\n" + dto.getClientMessage();

            // 2. Construimos el JSON (Payload) para Brevo
            Map<String, Object> body = Map.of(
                    // IMPORTANTE: En 'sender' poné el correo con el que te registraste en Brevo
                    "sender", Map.of("name", "Digital Soft Web", "email", "maritodufour76@gmail.com"),
                    // En 'to' poné el correo donde querés RECIBIR las notificaciones (tu Gmail personal)
                    "to", List.of(Map.of("email", "maritodufour76@gmail.com")),
                    "subject", "🟢 NUEVO LEAD - Digital Soft: " + dto.getBusinessName(),
                    "textContent", texto
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            // 3. Disparamos la petición HTTP por el puerto 443 (¡Libre de bloqueos!)
            restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            System.out.println("Notificación por correo enviada con éxito mediante Brevo API.");
        } catch (Exception e) {
            System.err.println("Error al enviar el correo por API: " + e.getMessage());
        }
    }
}