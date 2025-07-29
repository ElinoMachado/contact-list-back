package com.example.api.services;
import com.example.api.dto.*;
import com.example.api.entity.Contact;
import com.example.api.exception.ContactNotFoundException;
import com.example.api.exception.ContactAlreadyExistsException;
import com.example.api.repository.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Service
public class ContactService {
private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ContactDTO create(ContactRequestDTO dto) {
        if (repository.existsByMobile(dto.getMobile())) {
            throw new ContactAlreadyExistsException("Contact with mobile number already exists.");
        }

        Contact contact = Contact.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .mobile(dto.getMobile())
                .phone(dto.getPhone())
                .isFavorite(dto.isFavorite() ? "Y" : "N")
                .isActive("Y")
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(contact);
        return toDTO(contact);
    }

public Page<ContactDTO> findByStatusOrAll(String isActive, Pageable pageable) {
    Page<Contact> result;

    if (isActive == null) {
        result = repository.findAll(pageable);
    } else {
        result = repository.findByIsActive(isActive, pageable);
    }

    return result.map(this::toDTO);
}
    @Transactional
    public void deactivate(Long id) {
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
        contact.setIsActive("N");
    }

    @Transactional
    public ContactDTO update(Long id, ContactRequestDTO dto) {
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));

        contact.setName(dto.getName());
        contact.setEmail(dto.getEmail());
        contact.setPhone(dto.getPhone());
        contact.setIsFavorite(dto.isFavorite() ? "Y" : "N");
        return toDTO(contact);
    }

    private ContactDTO toDTO(Contact c) {
        ContactDTO dto = new ContactDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setEmail(c.getEmail());
        dto.setMobile(c.getMobile());
        dto.setPhone(c.getPhone());
        dto.setIsFavorite("Y".equals(c.getIsFavorite()));
        dto.setIsActive("Y".equals(c.getIsActive()));
        return dto;
    }
}
