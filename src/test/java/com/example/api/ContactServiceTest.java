package com.example.api;

import com.example.api.dto.ContactDTO;
import com.example.api.dto.ContactRequestDTO;
import com.example.api.entity.Contact;
import com.example.api.exception.ContactAlreadyExistsException;
import com.example.api.exception.ContactNotFoundException;
import com.example.api.repository.ContactRepository;
import com.example.api.services.ContactService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContactServiceTest {

    private ContactRepository repository;
    private ContactService service;

    @BeforeEach
    void setup() {
        repository = mock(ContactRepository.class);
        service = new ContactService(repository);
    }

    @Test
    void create_NewContact_Success() {
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setMobile("12345678901");
        dto.setPhone("1234567890");
        dto.setFavorite(true);
        dto.setActive(true);

        when(repository.existsByMobile(dto.getMobile())).thenReturn(false);

        ArgumentCaptor<Contact> captor = ArgumentCaptor.forClass(Contact.class);
        when(repository.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

        ContactDTO result = service.create(dto);

        Contact saved = captor.getValue();
        assertEquals(dto.getName(), saved.getName());
        assertEquals(dto.getEmail(), saved.getEmail());
        assertEquals(dto.getMobile(), saved.getMobile());
        assertEquals(dto.getPhone(), saved.getPhone());
        assertEquals("Y", saved.getIsFavorite());
        assertEquals("Y", saved.getIsActive());
        assertNotNull(saved.getCreatedAt());

        assertEquals(saved.getId(), result.getId()); // ID null here since not set in mock, that's OK
        assertEquals(saved.getName(), result.getName());
        assertEquals(saved.getEmail(), result.getEmail());
        assertEquals(saved.getMobile(), result.getMobile());
        assertEquals(saved.getPhone(), result.getPhone());
        assertTrue(result.getIsFavorite());
        assertTrue(result.getIsActive());
        assertEquals(saved.getCreatedAt(), result.getCreatedAt());
    }

    @Test
    void create_ExistingMobile_ThrowsException() {
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setMobile("12345678901");

        when(repository.existsByMobile(dto.getMobile())).thenReturn(true);

        assertThrows(ContactAlreadyExistsException.class, () -> service.create(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void findByFilters_ReturnsPageOfDTOs() {
        Contact contact = Contact.builder()
                .id(1L)
                .name("Jane")
                .email("jane@example.com")
                .mobile("10987654321")
                .phone("9876543210")
                .isFavorite("N")
                .isActive("Y")
                .createdAt(LocalDateTime.now())
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Contact> page = new PageImpl<>(List.of(contact));
        when(repository.findWithFilters("Jane", pageable)).thenReturn(page);

        Page<ContactDTO> result = service.findByFilters("Jane", pageable);

        assertEquals(1, result.getTotalElements());
        ContactDTO dto = result.getContent().get(0);
        assertEquals(contact.getId(), dto.getId());
        assertEquals(contact.getName(), dto.getName());
        assertEquals(contact.getEmail(), dto.getEmail());
        assertEquals(contact.getMobile(), dto.getMobile());
        assertEquals(contact.getPhone(), dto.getPhone());
        assertFalse(dto.getIsFavorite());
        assertTrue(dto.getIsActive());
        assertEquals(contact.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    void findById_ExistingContact_ReturnsDTO() {
        Contact contact = Contact.builder()
                .id(2L)
                .name("Alice")
                .email("alice@example.com")
                .mobile("11122233344")
                .phone("1231231234")
                .isFavorite("Y")
                .isActive("Y")
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.findById(2L)).thenReturn(Optional.of(contact));

        ContactDTO dto = service.findById(2L);

        assertEquals(contact.getId(), dto.getId());
        assertEquals(contact.getName(), dto.getName());
        assertEquals(contact.getEmail(), dto.getEmail());
        assertEquals(contact.getMobile(), dto.getMobile());
        assertEquals(contact.getPhone(), dto.getPhone());
        assertTrue(dto.getIsFavorite());
        assertTrue(dto.getIsActive());
        assertEquals(contact.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(repository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ContactNotFoundException.class, () -> service.findById(100L));
    }

    @Test
    void deactivate_ExistingContact_SetsInactive() {
        Contact contact = Contact.builder()
                .id(3L)
                .isActive("Y")
                .build();

        when(repository.findById(3L)).thenReturn(Optional.of(contact));

        service.deactivate(3L);

        assertEquals("N", contact.getIsActive());
    }

    @Test
    void deactivate_NotFound_ThrowsException() {
        when(repository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(ContactNotFoundException.class, () -> service.deactivate(4L));
    }

    @Test
    void update_ExistingContact_Success() {
        Contact contact = Contact.builder()
                .id(5L)
                .name("Old Name")
                .email("old@example.com")
                .mobile("55566677788")
                .phone("0001112222")
                .isFavorite("N")
                .isActive("Y")
                .createdAt(LocalDateTime.now())
                .build();

        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setName("New Name");
        dto.setEmail("new@example.com");
        dto.setMobile("55566677788");
        dto.setPhone("9998887777");
        dto.setFavorite(true);
        dto.setActive(false);

        when(repository.findById(5L)).thenReturn(Optional.of(contact));
        when(repository.save(contact)).thenAnswer(i -> i.getArgument(0));

        ContactDTO result = service.update(5L, dto);

        assertEquals(dto.getName(), contact.getName());
        assertEquals(dto.getEmail(), contact.getEmail());
        assertEquals(dto.getMobile(), contact.getMobile());
        assertEquals(dto.getPhone(), contact.getPhone());
        assertEquals("Y", contact.getIsFavorite());
        assertEquals("N", contact.getIsActive());

        assertEquals(contact.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getEmail(), result.getEmail());
        assertEquals(dto.getMobile(), result.getMobile());
        assertEquals(dto.getPhone(), result.getPhone());
        assertTrue(result.getIsFavorite());
        assertFalse(result.getIsActive());
    }

    @Test
    void update_NotFound_ThrowsException() {
        ContactRequestDTO dto = new ContactRequestDTO();
        when(repository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class, () -> service.update(10L, dto));
    }
    @Test
void create_NewContact_FavoriteAndActiveFalse() {
    ContactRequestDTO dto = new ContactRequestDTO();
    dto.setName("John Doe");
    dto.setEmail("john@example.com");
    dto.setMobile("12345678901");
    dto.setPhone("1234567890");
    dto.setFavorite(false);  // aqui
    dto.setActive(false);    // aqui

    when(repository.existsByMobile(dto.getMobile())).thenReturn(false);
    when(repository.save(any(Contact.class))).thenAnswer(i -> i.getArgument(0));

    ContactDTO result = service.create(dto);

    assertEquals("N", result.getIsFavorite() ? "Y" : "N".toUpperCase());  // false esperado
    assertEquals("N", result.getIsActive() ? "Y" : "N".toUpperCase());
    assertFalse(result.getIsFavorite());
    assertFalse(result.getIsActive());
}
@Test
void update_ExistingContact_FavoriteAndActiveFalse() {
    Contact contact = Contact.builder()
            .id(5L)
            .name("Old Name")
            .email("old@example.com")
            .mobile("55566677788")
            .phone("0001112222")
            .isFavorite("Y")
            .isActive("Y")
            .createdAt(LocalDateTime.now())
            .build();

    ContactRequestDTO dto = new ContactRequestDTO();
    dto.setName("New Name");
    dto.setEmail("new@example.com");
    dto.setMobile("55566677788");
    dto.setPhone("9998887777");
    dto.setFavorite(false);  // aqui
    dto.setActive(false);    // aqui

    when(repository.findById(5L)).thenReturn(Optional.of(contact));
    when(repository.save(contact)).thenAnswer(i -> i.getArgument(0));

    ContactDTO result = service.update(5L, dto);

    assertEquals("N", contact.getIsFavorite());
    assertEquals("N", contact.getIsActive());
    assertFalse(result.getIsFavorite());
    assertFalse(result.getIsActive());
}


}
