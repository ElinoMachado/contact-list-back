package com.example.api.controllers;

import com.example.api.dto.*;
import com.example.api.services.ContactService;
import jakarta.validation.Valid;

import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
public class ContactController {

   private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @PostMapping
public ResponseEntity<ContactDTO> create(@RequestBody @Valid ContactRequestDTO dto) {
    ContactDTO created = service.create(dto);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(created);
}

   @GetMapping
public ResponseEntity<Page<ContactDTO>> listByFilters(
        @RequestParam(required = false) String search,
        @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable
) {
    return ResponseEntity.ok(service.findByFilters(search, pageable));
}
    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> findById(@PathVariable Long id) {
        ContactDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> update(@PathVariable Long id, @RequestBody @Valid ContactRequestDTO dto) {
        ContactDTO updatedContact = service.update(id, dto);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
    service.deactivate(id);
    return ResponseEntity.noContent().build();
}
}
