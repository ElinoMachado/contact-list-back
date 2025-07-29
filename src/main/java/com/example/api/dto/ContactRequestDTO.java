package com.example.api.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
public class ContactRequestDTO {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    @Size(min = 11, max = 11)
    private String mobile;

    @Size(max = 10)
    private String phone;

    private boolean isFavorite = false;
    private boolean isActive = false;
}
