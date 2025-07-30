package com.example.api;

import com.example.api.controllers.ContactController;
import com.example.api.dto.*;
import com.example.api.services.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ContactDTO sampleContactDTO;
    private ContactRequestDTO sampleRequestDTO;

    @BeforeEach
    void setup() {
        sampleContactDTO = new ContactDTO();
        sampleContactDTO.setId(1L);
        sampleContactDTO.setName("John Doe");
        sampleContactDTO.setEmail("john@example.com");
        sampleContactDTO.setMobile("12345678901");
        sampleContactDTO.setPhone("1234567890");
        sampleContactDTO.setIsFavorite(true);
        sampleContactDTO.setIsActive(true);
        sampleContactDTO.setCreatedAt(LocalDateTime.now());

        sampleRequestDTO = new ContactRequestDTO();
        sampleRequestDTO.setName("John Doe");
        sampleRequestDTO.setEmail("john@example.com");
        sampleRequestDTO.setMobile("12345678901");
        sampleRequestDTO.setPhone("1234567890");
        sampleRequestDTO.setFavorite(true);
        sampleRequestDTO.setActive(true);
    }

    @Test
    void create_ShouldReturnCreatedContact() throws Exception {
        Mockito.when(service.create(any(ContactRequestDTO.class))).thenReturn(sampleContactDTO);

        mockMvc.perform(post("/api/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequestDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleContactDTO.getId()))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void listByFilters_ShouldReturnPagedContacts() throws Exception {
        Page<ContactDTO> page = new PageImpl<>(List.of(sampleContactDTO));

        Mockito.when(service.findByFilters(Mockito.anyString(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/contacts")
                .param("search", "John")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(sampleContactDTO.getId()))
            .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    void findById_ShouldReturnContact() throws Exception {
        Mockito.when(service.findById(1L)).thenReturn(sampleContactDTO);

        mockMvc.perform(get("/api/contacts/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleContactDTO.getId()))
            .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void update_ShouldReturnUpdatedContact() throws Exception {
        Mockito.when(service.update(eq(1L), any(ContactRequestDTO.class))).thenReturn(sampleContactDTO);

        mockMvc.perform(put("/api/contacts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequestDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleContactDTO.getId()))
            .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void deactivate_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(service).deactivate(1L);

        mockMvc.perform(delete("/api/contacts/1"))
            .andExpect(status().isOk());
    }
}
