package org.example.repository;

import org.example.domain.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // Make sure to import List

public interface StyleRepository extends JpaRepository<Style, Long> {

    // Add this method
    List<Style> findByCustomerId(Long customerId);
}
