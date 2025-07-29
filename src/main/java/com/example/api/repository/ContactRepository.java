package com.example.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api.entity.Contact;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    boolean existsByMobile(String mobile);
    Page<Contact> findByIsActive(String isActive, Pageable pageable);
}
