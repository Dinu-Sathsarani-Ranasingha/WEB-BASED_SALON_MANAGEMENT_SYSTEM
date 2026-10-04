package org.example.repository;

import org.example.domain.PackageItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageItemRepository extends JpaRepository<PackageItem, Long> {
}
