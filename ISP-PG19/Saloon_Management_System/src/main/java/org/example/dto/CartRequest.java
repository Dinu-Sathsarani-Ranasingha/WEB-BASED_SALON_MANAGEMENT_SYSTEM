package org.example.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartRequest {
    private List<Long> serviceIds;
    private List<Long> packageIds;
    private double totalAmount;
    private String customerName;
    private String customerPhone;
}