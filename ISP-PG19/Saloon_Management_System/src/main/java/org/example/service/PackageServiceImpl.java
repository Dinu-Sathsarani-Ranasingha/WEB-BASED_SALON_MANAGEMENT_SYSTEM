package org.example.service;

import org.example.domain.PackageCategory;
import org.example.domain.PackageItem;
import org.example.repository.PackageCategoryRepository;
import org.example.repository.PackageItemRepository;
import org.example.repository.CartItemRepository; // <-- Added this import
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {

    private final PackageCategoryRepository categoryRepository;
    private final PackageItemRepository itemRepository;
    private final CartItemRepository cartItemRepository; // <-- Added this repository

    @Override
    @Transactional
    public PackageCategory createCategory(PackageCategory category) {
        if (category.getItems() != null && !category.getItems().isEmpty()) {
            category.setTotalPrice(category.calculateTotalFromItems());
        }
        return categoryRepository.save(category);
    }

    @Override
    public List<PackageCategory> getAllCategories(boolean includeItems) {
        if (includeItems) {
            return categoryRepository.findAllWithItems();
        }
        return categoryRepository.findAll();
    }

    @Override
    public PackageCategory getCategoryById(Long id, boolean includeItems) {
        Optional<PackageCategory> opt;
        if (includeItems) {
            opt = categoryRepository.findByIdWithItems(id);
        } else {
            opt = categoryRepository.findById(id);
        }
        return opt.orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Override
    @Transactional
    public PackageItem createItem(PackageItem item) {
        if (item.getPackageCategory() == null || item.getPackageCategory().getId() == null) {
            throw new IllegalArgumentException("Package category ID is required");
        }
        PackageCategory category = categoryRepository.findById(item.getPackageCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + item.getPackageCategory().getId()));
        item.setPackageCategory(category);
        return itemRepository.save(item);
    }

    @Override
    public List<PackageItem> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public PackageItem getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }

    @Override
    @Transactional
    public PackageItem updateItem(Long id, PackageItem item) {
        PackageItem existing = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        existing.setName(item.getName());
        existing.setPrice(item.getPrice());
        existing.setAvailability(item.getAvailability());
        if (item.getPackageCategory() != null && item.getPackageCategory().getId() != null) {
            PackageCategory category = categoryRepository.findById(item.getPackageCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existing.setPackageCategory(category);
        }
        return itemRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found with id: " + id);
        }
        itemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PackageCategory updateCategory(Long id, PackageCategory category) {
        PackageCategory existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        existing.setPackageType(category.getPackageType());
        existing.setStatus(category.getStatus());
        existing.setTotalPrice(category.getTotalPrice());
        return categoryRepository.save(existing);
    }

    // --- THIS IS THE UPDATED DELETE METHOD ---
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }

        // 1. Clear out references in the database Cart table first to avoid the SQL Error 547
        cartItemRepository.deleteByPackageCategoryId(id);

        // 2. Now safely delete the category (Hibernate will cascade-delete the PackageItems)
        categoryRepository.deleteById(id);
    }
}