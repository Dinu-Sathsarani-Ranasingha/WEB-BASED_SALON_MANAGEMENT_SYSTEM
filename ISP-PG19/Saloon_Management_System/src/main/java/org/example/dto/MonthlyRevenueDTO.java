package org.example.dto;

public class MonthlyRevenueDTO {
    private int month;
    private double totalRevenue;

    public MonthlyRevenueDTO(int month, double totalRevenue) {
        this.month = month;
        this.totalRevenue = totalRevenue;
    }

    // Getters and Setters
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
}
