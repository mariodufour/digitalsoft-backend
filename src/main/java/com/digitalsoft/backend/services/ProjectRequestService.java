package com.digitalsoft.backend.services;

import com.digitalsoft.backend.dtos.NewProjectRequestDTO;
import com.digitalsoft.backend.entities.Client;
import com.digitalsoft.backend.entities.ProjectRequest;
import com.digitalsoft.backend.entities.ServiceCategory;
import com.digitalsoft.backend.repositories.ClientRepository;
import com.digitalsoft.backend.repositories.ProjectRequestRepository;
import com.digitalsoft.backend.repositories.ServiceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // Lombok genera el constructor para la inyección de dependencias
public class ProjectRequestService {

    private final ProjectRequestRepository projectRequestRepository;
    private final ClientRepository clientRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final JavaMailSender mailSender;

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

    // Método privado que arma y manda el mail
    private void enviarNotificacion(NewProjectRequestDTO dto) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom("maritodufour76@gmail.com");
            mensaje.setTo("maritodufour76@gmail.com");
            mensaje.setSubject("🟢 NUEVO LEAD - Digital Soft: " + dto.getBusinessName());

            String texto = "¡Recibiste una nueva solicitud de desarrollo!\n\n"
                    + "👨‍💼 Cliente: " + dto.getContactName() + "\n"
                    + "🏢 Negocio: " + dto.getBusinessName() + "\n"
                    + "📱 Teléfono: " + dto.getPhone() + "\n"
                    + "✉️ Email: " + (dto.getEmail() != null && !dto.getEmail().isEmpty() ? dto.getEmail() : "No especificado") + "\n\n"
                    + "💬 Mensaje del cliente:\n" + dto.getClientMessage();

            mensaje.setText(texto);
            mailSender.send(mensaje);

            System.out.println("Notificación por correo enviada con éxito.");
        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}