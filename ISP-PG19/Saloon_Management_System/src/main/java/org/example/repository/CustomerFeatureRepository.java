package org.example.repository;

import org.example.domain.CustomerFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerFeatureRepository extends JpaRepository<CustomerFeature, Long> {
    List<CustomerFeature> findByCustomerId(Long customerId);
}
