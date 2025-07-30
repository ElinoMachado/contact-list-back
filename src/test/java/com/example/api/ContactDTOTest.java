package com.example.api;

import org.junit.jupiter.api.Test;

import com.example.api.dto.ContactDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ContactDTOTest {

    @Test
    void testGettersAndSetters() {
        ContactDTO dto = new ContactDTO();

        // Testa set e get do id
        dto.setId(123L);
        assertEquals(123L, dto.getId());

        // Testa set e get do name
        dto.setName("John Doe");
        assertEquals("John Doe", dto.getName());

        // Testa set e get do email
        dto.setEmail("john@example.com");
        assertEquals("john@example.com", dto.getEmail());

        // Testa set e get do mobile
        dto.setMobile("123456789");
        assertEquals("123456789", dto.getMobile());

        // Testa set e get do phone
        dto.setPhone("987654321");
        assertEquals("987654321", dto.getPhone());

        // Testa set e get do createdAt
        LocalDateTime now = LocalDateTime.now();
        dto.setCreatedAt(now);
        assertEquals(now, dto.getCreatedAt());

        // Testa set e get do isActive via métodos customizados
        dto.setIsActive(true);
        assertTrue(dto.getIsActive());

        dto.setIsActive(false);
        assertFalse(dto.getIsActive());

        // Testa set e get do isFavorite via métodos customizados
        dto.setIsFavorite(true);
        assertTrue(dto.getIsFavorite());

        dto.setIsFavorite(false);
        assertFalse(dto.getIsFavorite());
    }
    @Test
void testEqualsAndHashCode() {
    LocalDateTime now = LocalDateTime.now();

    ContactDTO dto1 = new ContactDTO();
    dto1.setId(1L);
    dto1.setName("Alice");
    dto1.setEmail("alice@example.com");
    dto1.setMobile("12345678901");
    dto1.setPhone("1234567890");
    dto1.setCreatedAt(now);
    dto1.setIsFavorite(true);
    dto1.setIsActive(true);

    ContactDTO dto2 = new ContactDTO();
    dto2.setId(1L);
    dto2.setName("Alice");
    dto2.setEmail("alice@example.com");
    dto2.setMobile("12345678901");
    dto2.setPhone("1234567890");
    dto2.setCreatedAt(now);
    dto2.setIsFavorite(true);
    dto2.setIsActive(true);

    ContactDTO dto3 = new ContactDTO();
    dto3.setId(2L);
    dto3.setName("Bob");

    assertEquals(dto1, dto2);                      // Mesmo conteúdo
    assertNotEquals(dto1, dto3);                   // Conteúdo diferente

    assertEquals(dto1.hashCode(), dto2.hashCode()); // Mesmo conteúdo = mesmo hash
    assertNotEquals(dto1.hashCode(), dto3.hashCode());
}

}
