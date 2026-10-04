package org.example.controller;

import org.example.service.RatingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rating")
public class RatingController {

    private final RatingService service;

    public RatingController(RatingService service) {
        this.service = service;
    }

    // Redirect /rating to the combined feedback+rating page
    @GetMapping
    public String redirectToFeedback() {
        return "redirect:/feedback";
    }



    // Redirect /rating/create to combined page
    @GetMapping("/create")
    public String redirectCreate() {
        return "redirect:/feedback";
    }
}
