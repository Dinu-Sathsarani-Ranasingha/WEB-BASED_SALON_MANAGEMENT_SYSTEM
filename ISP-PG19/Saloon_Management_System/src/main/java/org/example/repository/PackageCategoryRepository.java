package org.example.repository;
import org.example.domain.PackageCategory; // Update this import!

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageCategoryRepository extends JpaRepository<PackageCategory, Long> {

    @Query("SELECT c FROM PackageCategory c LEFT JOIN FETCH c.items")
    List<PackageCategory> findAllWithItems();

    @Query("SELECT c FROM PackageCategory c LEFT JOIN FETCH c.items WHERE c.id = :id")
    Optional<PackageCategory> findByIdWithItems(Long id);
}
