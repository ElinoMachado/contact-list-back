package com.example.api.dto;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;

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

     @JsonProperty("isFavorite")
    private boolean favorite = false;

    @JsonProperty("isActive")
    private boolean active = false;
}
