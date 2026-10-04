package org.example.repository;

import org.example.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Safely removes the item from all carts if an admin deletes the package
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.packageCategory.id = :packageId")
    void deleteByPackageCategoryId(@Param("packageId") Long packageId);
}