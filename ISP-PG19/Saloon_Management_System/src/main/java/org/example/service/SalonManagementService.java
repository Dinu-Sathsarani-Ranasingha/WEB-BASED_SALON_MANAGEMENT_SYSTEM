package org.example.service;

import org.example.domain.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class SalonManagementService {

    @Autowired
    private ServiceCategoryRepository categoryRepo;
    @Autowired
    private SalonServiceRepository serviceRepo;
    @Autowired
    private ServiceStyleRepository styleRepo;

    // --- 1. CATEGORY OPERATIONS (With Image Upload) ---

    private static final String UPLOAD_DIR = "src/main/resources/static/images/categories/";

    public ServiceCategory saveCategory(String name, String description, MultipartFile file) throws IOException {
        ServiceCategory category = new ServiceCategory();
        category.setCategoryName(name);
        category.setCategoryDescription(description);

        if (file != null && !file.isEmpty()) {

            // --- NEW CODE: Check if folder exists, if not, create it! ---
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // ------------------------------------------------------------

            // Generate unique filename and save to folder
            String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + uniqueFilename);
            Files.write(filePath, file.getBytes());
            category.setCategoryImage(uniqueFilename);
        } else {
            category.setCategoryImage("default.png");
        }
        return categoryRepo.save(category);
    }

    public ServiceCategory updateCategory(Long id, String name, String description, MultipartFile file) throws IOException {
        // 1. Find the existing category
        ServiceCategory existingCategory = categoryRepo.findById(id).orElseThrow();

        // 2. Update the text fields
        existingCategory.setCategoryName(name);
        existingCategory.setCategoryDescription(description);

        // 3. Update the image ONLY if a new file was uploaded
        if (file != null && !file.isEmpty()) {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + uniqueFilename);
            Files.write(filePath, file.getBytes());

            existingCategory.setCategoryImage(uniqueFilename);
        }

        // 4. Save and return
        return categoryRepo.save(existingCategory);
    }

    public List<ServiceCategory> getAllCategories() {
        return categoryRepo.findAll();
    }

    // --- DELETE OPERATIONS ---
    public void deleteCategory(Long id) {
        ServiceCategory category = categoryRepo.findById(id).orElseThrow();
        // Delete the image file if it's not the default one
        if (category.getCategoryImage() != null && !category.getCategoryImage().equals("default.png")) {
            try {
                Path filePath = Paths.get(UPLOAD_DIR + category.getCategoryImage());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.out.println("Could not delete image file: " + e.getMessage());
            }
        }
        categoryRepo.deleteById(id);
    }

    // --- 2. SALON SERVICE OPERATIONS ---

    public SalonService saveSalonService(SalonService service) {
        return serviceRepo.save(service);
    }

    public List<SalonService> getAllSalonServices() {
        return serviceRepo.findAll();
    }

    public List<SalonService> getServicesByCategoryId(Long categoryId) {
        return serviceRepo.findByServiceCategory_CategoryId(categoryId);
    }

    // --- UPDATE SERVICE ---
    public SalonService updateSalonService(Long id, SalonService serviceDetails) {
        SalonService existingService = serviceRepo.findById(id).orElseThrow();
        existingService.setServiceName(serviceDetails.getServiceName());
        return serviceRepo.save(existingService);
    }

    public void deleteSalonService(Long id) {
        serviceRepo.deleteById(id);
    }

    // --- 3. SERVICE STYLE OPERATIONS ---

    public ServiceStyle saveServiceStyle(ServiceStyle style) {
        return styleRepo.save(style);
    }

    public List<ServiceStyle> getAllServiceStyles() {
        return styleRepo.findAll();
    }

    public ServiceStyle updateServiceStyle(Long id, ServiceStyle styleDetails) {
        // Find the existing style, update its fields, and save it back
        ServiceStyle existingStyle = styleRepo.findById(id).orElseThrow();
        existingStyle.setStyleName(styleDetails.getStyleName());
        existingStyle.setPrice(styleDetails.getPrice());
        existingStyle.setAvailability(styleDetails.getAvailability());
        return styleRepo.save(existingStyle);
    }

    public void deleteServiceStyle(Long id) {
        styleRepo.deleteById(id);
    }
}
