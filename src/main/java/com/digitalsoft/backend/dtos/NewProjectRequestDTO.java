package com.digitalsoft.backend.dtos;

import lombok.Data;

@Data
public class NewProjectRequestDTO {

    private String businessName;
    private String contactName;
    private String phone;
    private String email;

    // ID del servicio que eligió en la web
    private Long serviceCategoryId;

    // El mensaje donde cuenta qué necesita
    private String clientMessage;
}