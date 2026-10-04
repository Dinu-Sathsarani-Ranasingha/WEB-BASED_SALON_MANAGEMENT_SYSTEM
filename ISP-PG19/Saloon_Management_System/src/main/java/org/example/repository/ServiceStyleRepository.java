package org.example.repository;
import org.example.domain.ServiceStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceStyleRepository extends JpaRepository<ServiceStyle, Long> {
}
