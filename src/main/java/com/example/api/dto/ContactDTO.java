package com.example.api.dto;

import lombok.*;

@Data
public class ContactDTO {
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String phone;
    private boolean isFavorite;
    private boolean isActive;

    public Boolean getIsActive() {
    return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

     public Boolean getIsFavorite() {
    return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
