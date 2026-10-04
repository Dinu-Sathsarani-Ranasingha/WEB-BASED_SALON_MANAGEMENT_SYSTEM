package org.example.controller;
import jakarta.validation.Valid;
import org.example.domain.PackageCategory;
import org.example.domain.PackageItem;
import org.example.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    // ========== CATEGORY CRUD ==========

    @PostMapping("/categories")
    public ResponseEntity<PackageCategory> createCategory(@Valid @RequestBody PackageCategory category) {
        return ResponseEntity.ok(packageService.createCategory(category));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<PackageCategory>> getAllCategories(
            @RequestParam(defaultValue = "false") boolean includeItems) {
        return ResponseEntity.ok(packageService.getAllCategories(includeItems));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<PackageCategory> getCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean includeItems) {
        return ResponseEntity.ok(packageService.getCategoryById(id, includeItems));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<PackageCategory> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody PackageCategory category) {
        return ResponseEntity.ok(packageService.updateCategory(id, category));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        packageService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    // ========== ITEM CRUD ==========

    @PostMapping("/items")
    public ResponseEntity<PackageItem> createItem(@Valid @RequestBody PackageItem item) {
        return ResponseEntity.ok(packageService.createItem(item));
    }

    @GetMapping("/items")
    public ResponseEntity<List<PackageItem>> getAllItems() {
        return ResponseEntity.ok(packageService.getAllItems());
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<PackageItem> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(packageService.getItemById(id));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<PackageItem> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody PackageItem item) {
        return ResponseEntity.ok(packageService.updateItem(id, item));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        packageService.deleteItem(id);
        return ResponseEntity.ok().build();
    }
}