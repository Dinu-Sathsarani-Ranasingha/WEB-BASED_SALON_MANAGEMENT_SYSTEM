package org.example.controller;

import org.example.domain.Feedback;
import org.example.domain.Rating;
import org.example.service.FeedbackService;
import org.example.service.RatingService;
import org.example.service.EmployeeService;
import org.example.service.SalonManagementService;
import org.example.service.CustomUserDetails;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final RatingService ratingService;
    private final EmployeeService employeeService;
    private final SalonManagementService salonManagementService;

    public FeedbackController(FeedbackService feedbackService,
                              RatingService ratingService,
                              EmployeeService employeeService,
                              SalonManagementService salonManagementService) {
        this.feedbackService = feedbackService;
        this.ratingService = ratingService;
        this.employeeService = employeeService;
        this.salonManagementService = salonManagementService;
    }

    private void populateDropdowns(Model model) {
        model.addAttribute("staffList", employeeService.findAll());
        model.addAttribute("serviceStyles", salonManagementService.getAllServiceStyles());
    }

    @GetMapping
    public String combinedPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long customerId = userDetails.getCustomerId();

        model.addAttribute("feedbacks", feedbackService.getByCustomerId(customerId));
        model.addAttribute("ratings",   ratingService.getByCustomerId(customerId));

        model.addAttribute("feedback",  new Feedback());
        model.addAttribute("rating",    new Rating());
        populateDropdowns(model);
        return "feedback/list";
    }

    @PostMapping("/create")
    public String saveComment(@Valid @ModelAttribute("feedback") Feedback feedback,
                              BindingResult result,
                              Model model,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long customerId = userDetails.getCustomerId();

        if (result.hasErrors()) {
            model.addAttribute("feedbacks", feedbackService.getByCustomerId(customerId));
            model.addAttribute("ratings", ratingService.getByCustomerId(customerId));
            model.addAttribute("rating", new Rating());
            model.addAttribute("showCommentForm", true);
            populateDropdowns(model);
            return "feedback/list";
        }

        // SAFE PATTERN: Explicitly create a brand new entity to guarantee an INSERT
        Feedback newFeedback = new Feedback();
        newFeedback.setName(feedback.getName());
        newFeedback.setComment(feedback.getComment());
        newFeedback.setService(feedback.getService());
        newFeedback.setStaffMember(feedback.getStaffMember());
        newFeedback.setCustomerId(customerId);
        newFeedback.setDate(LocalDate.now());

        feedbackService.save(newFeedback);
        return "redirect:/feedback";
    }

    @PostMapping("/rating/create")
    public String saveRating(@Valid @ModelAttribute("rating") Rating rating,
                             BindingResult result,
                             Model model,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long customerId = userDetails.getCustomerId();

        if (result.hasErrors()) {
            model.addAttribute("feedbacks", feedbackService.getByCustomerId(customerId));
            model.addAttribute("ratings", ratingService.getByCustomerId(customerId));
            model.addAttribute("feedback", new Feedback());
            model.addAttribute("showRatingForm", true);
            populateDropdowns(model);
            return "feedback/list";
        }

        // SAFE PATTERN: Explicitly create a brand new entity to guarantee an INSERT
        Rating newRating = new Rating();
        newRating.setName(rating.getName());
        newRating.setRating(rating.getRating());
        newRating.setService(rating.getService());
        newRating.setStaffMember(rating.getStaffMember());
        newRating.setCustomerId(customerId);

        ratingService.save(newRating);
        return "redirect:/feedback";
    }

    @GetMapping("/edit/{id}")
    public String editCommentForm(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long customerId = userDetails.getCustomerId();

        model.addAttribute("feedback",  feedbackService.getById(id));
        model.addAttribute("feedbacks", feedbackService.getByCustomerId(customerId));
        model.addAttribute("ratings",   ratingService.getByCustomerId(customerId));
        model.addAttribute("rating",    new Rating());
        model.addAttribute("showCommentForm", true);
        populateDropdowns(model);
        return "feedback/list";
    }

    @PostMapping("/edit/{id}")
    public String updateComment(@PathVariable Long id,
                                @Valid @ModelAttribute("feedback") Feedback feedback,
                                BindingResult result,
                                Model model,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long customerId = userDetails.getCustomerId();

        if (result.hasErrors()) {
            model.addAttribute("feedbacks", feedbackService.getByCustomerId(customerId));
            model.addAttribute("ratings", ratingService.getByCustomerId(customerId));
            model.addAttribute("rating", new Rating());
            model.addAttribute("showCommentForm", true);
            populateDropdowns(model);
            return "feedback/list";
        }

        // SAFE PATTERN: Fetch existing and apply updates for a clean UPDATE
        Feedback existing = feedbackService.getById(id);
        existing.setName(feedback.getName());
        existing.setComment(feedback.getComment());
        existing.setService(feedback.getService());
        existing.setStaffMember(feedback.getStaffMember());
        existing.setDate(LocalDate.now());

        feedbackService.save(existing);
        return "redirect:/feedback";
    }

    @GetMapping("/rating/edit/{id}")
    public String editRatingForm(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long customerId = userDetails.getCustomerId();

        model.addAttribute("rating",    ratingService.getById(id));
        model.addAttribute("feedbacks", feedbackService.getByCustomerId(customerId));
        model.addAttribute("ratings",   ratingService.getByCustomerId(customerId));
        model.addAttribute("feedback",  new Feedback());
        model.addAttribute("showRatingForm", true);
        populateDropdowns(model);
        return "feedback/list";
    }

    @PostMapping("/rating/edit/{id}")
    public String updateRating(@PathVariable Long id,
                               @Valid @ModelAttribute("rating") Rating rating,
                               BindingResult result,
                               Model model,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long customerId = userDetails.getCustomerId();

        if (result.hasErrors()) {
            model.addAttribute("feedbacks", feedbackService.getByCustomerId(customerId));
            model.addAttribute("ratings", ratingService.getByCustomerId(customerId));
            model.addAttribute("feedback", new Feedback());
            model.addAttribute("showRatingForm", true);
            populateDropdowns(model);
            return "feedback/list";
        }

        ratingService.update(id, rating);
        return "redirect:/feedback";
    }

    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        feedbackService.delete(id);
        return "redirect:/feedback";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable Long id) {
        ratingService.delete(id);
        return "redirect:/feedback";
    }

    // ── Show Highlights / Recommendations ─────────────────────────────────
    @GetMapping("/highlights")
    public String showHighlights(Model model) {
        // Fetch and reverse lists so the newest entries appear at the top!
        List<Feedback> allFeedbacks = feedbackService.getAll();
        Collections.reverse(allFeedbacks);
        model.addAttribute("feedbacks", allFeedbacks);

        List<Rating> allRatings = ratingService.getAll();
        Collections.reverse(allRatings);
        model.addAttribute("ratings", allRatings);

        return "feedback/recommendations";
    }
}