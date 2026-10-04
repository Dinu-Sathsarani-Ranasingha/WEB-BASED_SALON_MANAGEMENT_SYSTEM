package org.example.service;

import org.example.domain.PackageCategory;
import org.example.domain.PackageItem;

import java.util.List;

public interface PackageService {

    // Category CRUD
    PackageCategory createCategory(PackageCategory category);
    List<PackageCategory> getAllCategories(boolean includeItems);
    PackageCategory getCategoryById(Long id, boolean includeItems);
    PackageCategory updateCategory(Long id, PackageCategory category);
    void deleteCategory(Long id);

    // Item CRUD
    PackageItem createItem(PackageItem item);
    List<PackageItem> getAllItems();
    PackageItem getItemById(Long id);
    PackageItem updateItem(Long id, PackageItem item);
    void deleteItem(Long id);
}
