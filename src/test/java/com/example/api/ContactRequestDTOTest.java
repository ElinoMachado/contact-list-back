package com.example.api;

import org.junit.jupiter.api.Test;

import com.example.api.dto.ContactRequestDTO;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.*;
import java.util.Set;

class ContactRequestDTOTest {

    private final Validator validator;

    public ContactRequestDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDTO() {
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setMobile("12345678901"); // 11 chars
        dto.setPhone("1234567890");   // max 10 chars
        dto.setActive(true);
        dto.setFavorite(true);

        Set<ConstraintViolation<ContactRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());

        assertEquals("John Doe", dto.getName());
        assertEquals("john@example.com", dto.getEmail());
        assertEquals("12345678901", dto.getMobile());
        assertEquals("1234567890", dto.getPhone());
        assertTrue(dto.isActive());
        assertTrue(dto.isFavorite());
    }

    @Test
    void testInvalidDTO_BlankName() {
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setName(""); // blank name
        dto.setEmail("john@example.com");
        dto.setMobile("12345678901");
        dto.setPhone("1234567890");

        Set<ConstraintViolation<ContactRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testInvalidDTO_InvalidEmail() {
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setName("John Doe");
        dto.setEmail("invalid-email");
        dto.setMobile("12345678901");
        dto.setPhone("1234567890");

        Set<ConstraintViolation<ContactRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testInvalidDTO_MobileWrongSize() {
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setMobile("12345"); // too short
        dto.setPhone("1234567890");

        Set<ConstraintViolation<ContactRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("mobile")));
    }

    @Test
    void testInvalidDTO_PhoneTooLong() {
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setMobile("12345678901");
        dto.setPhone("123456789012345"); // longer than 10

        Set<ConstraintViolation<ContactRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phone")));
    }
    @Test
void testEqualsAndHashCode() {
    ContactRequestDTO dto1 = new ContactRequestDTO();
    dto1.setName("Alice");
    dto1.setEmail("alice@example.com");
    dto1.setMobile("12345678901");
    dto1.setPhone("1234567890");
    dto1.setActive(true);
    dto1.setFavorite(true);

    ContactRequestDTO dto2 = new ContactRequestDTO();
    dto2.setName("Alice");
    dto2.setEmail("alice@example.com");
    dto2.setMobile("12345678901");
    dto2.setPhone("1234567890");
    dto2.setActive(true);
    dto2.setFavorite(true);

    ContactRequestDTO dto3 = new ContactRequestDTO();
    dto3.setName("Bob");
    dto3.setEmail("bob@example.com");
    dto3.setMobile("09876543210");
    dto3.setPhone("123");
    dto3.setActive(false);
    dto3.setFavorite(false);

    // equals
    assertEquals(dto1, dto2);
    assertNotEquals(dto1, dto3);

    // hashCode
    assertEquals(dto1.hashCode(), dto2.hashCode());
    assertNotEquals(dto1.hashCode(), dto3.hashCode());
}

}
