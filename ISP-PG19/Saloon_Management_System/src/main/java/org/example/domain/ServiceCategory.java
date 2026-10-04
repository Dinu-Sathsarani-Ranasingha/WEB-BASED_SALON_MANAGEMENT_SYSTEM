package org.example.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Service_Category")
@Getter
@Setter// <-- This single Lombok annotation does all the work!
public class ServiceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryID")
    private Long categoryId;


    @NotBlank(message = "Category name cannot be empty")
    @Column(name = "Category_Name")
    private String categoryName;

    @Lob
    @NotBlank(message = "Category description cannot be empty")
    @Column(name = "Category_Description")
    private String categoryDescription;

    @Column(name = "Category_Image")
    private String categoryImage;

    @OneToMany(mappedBy = "serviceCategory", cascade = CascadeType.ALL)
    private List<SalonService> salonServices;

}
