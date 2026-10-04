package org.example.repository;
import org.example.domain.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SalonServiceRepository extends JpaRepository<SalonService, Long> {
    // Spring Boot automatically writes the SQL for this based on the method name!
    List<SalonService> findByServiceCategory_CategoryId(Long categoryId);
}
