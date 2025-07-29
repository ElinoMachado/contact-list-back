package com.example.api.controllers;

import com.example.api.dto.*;
import com.example.api.services.ContactService;
import jakarta.validation.Valid;

import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ContactDTO create(@RequestBody @Valid ContactRequestDTO dto) {
        return service.create(dto);
    }

@GetMapping
public ResponseEntity<Page<ContactDTO>> listByStatus(
        @RequestParam(required = false) String isActive,
        @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable
) {
    return ResponseEntity.ok(service.findByStatusOrAll(isActive, pageable));
}
//test
    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> update(@PathVariable Long id, @RequestBody @Valid ContactRequestDTO dto) {
        ContactDTO updatedContact = service.update(id, dto);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        service.deactivate(id);
    }
}
