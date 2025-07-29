package com.example.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contact", schema = "contact_management")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "mobile_number", nullable = false, length = 11, unique = true)
    private String mobile;

    @Column(name = "phone_number", length = 10)
    private String phone;

    @Column(name = "is_favorite", nullable = false, length = 1)
    private String isFavorite; // 'Y' or 'N'

    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive; // 'Y' or 'N'

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
