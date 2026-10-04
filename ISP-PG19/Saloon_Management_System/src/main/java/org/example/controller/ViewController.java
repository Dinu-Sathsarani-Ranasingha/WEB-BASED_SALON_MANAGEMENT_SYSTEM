package org.example.controller;

import org.example.domain.Feedback;
import org.example.domain.Rating;
import org.example.service.FeedbackService;
import org.example.service.RatingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class ViewController {

    private final FeedbackService feedbackService;
    private final RatingService ratingService;

    // Inject BOTH services
    public ViewController(FeedbackService feedbackService, RatingService ratingService) {
        this.feedbackService = feedbackService;
        this.ratingService = ratingService;
    }

    // --- HOME PAGE WITH DYNAMIC REVIEWS ---
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("feedbacks", feedbackService.getTop3RecentFeedbacks());
        return "home"; // Maps to home.html
    }

    // --- NEW: PUBLIC REVIEWS PAGE ---
    @GetMapping("/reviews")
    public String publicReviews(Model model) {
        List<Feedback> allFeedbacks = feedbackService.getAll();
        Collections.reverse(allFeedbacks); // Newest first

        List<Rating> allRatings = ratingService.getAll();
        Collections.reverse(allRatings); // Newest first

        // Calculate average rating
        double averageRating = 0.0;
        if (!allRatings.isEmpty()) {
            double sum = 0;
            for (Rating r : allRatings) {
                sum += r.getRating();
            }
            averageRating = sum / allRatings.size();
        }

        model.addAttribute("feedbacks", allFeedbacks);
        model.addAttribute("ratings", allRatings);
        model.addAttribute("averageRating", String.format("%.1f", averageRating));
        model.addAttribute("totalRatings", allRatings.size());

        return "public-reviews";
    }

    // --- PUBLIC PATHS ---
    @GetMapping("/services")
    public String publicServices() {
        return "public-services";
    }

    @GetMapping("/packages")
    public String publicPackages() {
        return "public-packages";
    }

    @GetMapping("/styles")
    public String publicStyles() {
        return "public-styles";
    }

    // --- ADMIN PATHS FOR SERVICES & PACKAGES ---
    @GetMapping("/admin/manage-categories")
    public String manageCategories() {
        return "admin_services";
    }

    @GetMapping("/admin/manage-services")
    public String adminServices() {
        return "services";
    }

    @GetMapping("/admin/manage-packages")
    public String managePackages() {
        return "packages-admin";
    }

    @GetMapping("/admin/daily-appointments")
    public String dailyAppointments() {
        return "daily-appointments";
    }

    // --- CUSTOMER PATHS ---
    @GetMapping("/customer/services")
    public String customerServices() {
        return "customer-services";
    }

    @GetMapping("/customer/packages")
    public String customerPackages() {
        return "customer-packages";
    }

    @GetMapping("/customer/book")
    public String bookAppointment() {
        return "appointment";
    }

    @GetMapping("/customer/payment")
    public String payment() {
        return "payment";
    }

    @GetMapping("/appointments/my-history")
    public String myHistory() {
        return "history";
    }

    @GetMapping("/customer/purchase-history") //sprint3
    public String purchaseHistory() {
        return "purchase-history";
    }

    @GetMapping("/staff/schedule")
    public String staffSchedule() {
        return "daily-appointments";
    }
}