package org.example.controller;

import jakarta.validation.Valid;
import org.example.domain.ServiceCategory;
import org.example.domain.SalonService;
import org.example.domain.ServiceStyle;
import org.example.service.SalonManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/salon")
public class SalonController {

    @Autowired
    private SalonManagementService salonService;

    // ==========================================
    // 1. CATEGORY ENDPOINTS (With Image Upload)
    // ==========================================

    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile file) {

        // VALIDATION: Check both name and description
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category name is required.");
        }
        if (description == null || description.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category description is required.");
        }

        try {
            ServiceCategory newCategory = salonService.saveCategory(name, description, file);
            return ResponseEntity.ok(newCategory);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile file) {

        // VALIDATION: Ensure editing doesn't allow saving empty strings
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category name is required.");
        }
        if (description == null || description.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category description is required.");
        }

        try {
            ServiceCategory updatedCategory = salonService.updateCategory(id, name, description, file);
            return ResponseEntity.ok(updatedCategory);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categories")
    public List<ServiceCategory> getCategories() {
        return salonService.getAllCategories();
    }

    // --- NEW: Delete Endpoints ---
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        salonService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    // ==========================================
    // 2. SALON SERVICE ENDPOINTS
    // ==========================================

    // 2. Service Check (Using @Valid)
    @PostMapping("/services")
    public ResponseEntity<?> addService(@Valid @RequestBody SalonService service) {
        return ResponseEntity.ok(salonService.saveSalonService(service));
    }

    @GetMapping("/categories/{categoryId}/services")
    public List<SalonService> getServicesByCategory(@PathVariable Long categoryId) {
        return salonService.getServicesByCategoryId(categoryId);
    }

    @GetMapping("/services")
    public List<SalonService> getAllServices() {
        return salonService.getAllSalonServices();
    }

    // --- NEW: Update Service ---
    @PutMapping("/services/{id}")
    public ResponseEntity<?> updateService(@PathVariable Long id, @Valid @RequestBody SalonService serviceDetails) {
        return ResponseEntity.ok(salonService.updateSalonService(id, serviceDetails));
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        salonService.deleteSalonService(id);
        return ResponseEntity.ok().build();
    }

    // ==========================================
    // 3. SERVICE STYLE ENDPOINTS
    // ==========================================

    // 3. Style Check (Using @Valid)
    @PostMapping("/styles")
    public ResponseEntity<?> addStyle(@Valid @RequestBody ServiceStyle style) {
        return ResponseEntity.ok(salonService.saveServiceStyle(style));
    }

    @PutMapping("/styles/{id}")
    public ResponseEntity<?> updateStyle(@PathVariable Long id, @Valid @RequestBody ServiceStyle styleDetails) {
        // Now it checks the @NotBlank and @Positive rules before saving!
        return ResponseEntity.ok(salonService.updateServiceStyle(id, styleDetails));
    }

    @GetMapping("/styles")
    public List<ServiceStyle> getAllStyles() {
        return salonService.getAllServiceStyles();
    }

    @DeleteMapping("/styles/{id}")
    public ResponseEntity<?> deleteStyle(@PathVariable Long id) {
        salonService.deleteServiceStyle(id);
        return ResponseEntity.ok().build();
    }
}