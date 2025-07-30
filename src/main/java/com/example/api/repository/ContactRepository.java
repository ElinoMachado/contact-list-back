package com.example.api.repository;

import com.example.api.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    boolean existsByMobile(String mobile);

    @Query("""
    SELECT c FROM Contact c
    WHERE 
        (:search = '' OR 
         LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR 
         LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR 
         c.mobile LIKE CONCAT('%', :search, '%') OR 
         c.phone LIKE CONCAT('%', :search, '%'))
    ORDER BY 
        CASE WHEN c.isFavorite = 'Y' THEN 0 ELSE 1 END, 
        CASE WHEN c.isActive = 'N' THEN 2 ELSE 0 END,
        c.createdAt DESC,
        c.name
""")
Page<Contact> findWithFilters(@Param("search") String search, Pageable pageable);
}
