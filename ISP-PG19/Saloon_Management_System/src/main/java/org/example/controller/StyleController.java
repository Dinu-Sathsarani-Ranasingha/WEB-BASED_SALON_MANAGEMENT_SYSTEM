package org.example.controller;

import org.example.domain.CustomerFeature;
import org.example.domain.StyleRequest;
import org.example.service.StyleRecommendationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.example.service.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/style")
public class StyleController {

    private final StyleRecommendationService service;

    public StyleController(StyleRecommendationService service) {
        this.service = service;
    }

    // GET /style — show blank recommendation form
    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("styleRequest", new StyleRequest());
        return "style/rform";
    }

    // POST /style/recommend — save features + generate recommendations
    @PostMapping("/recommend")
    public String getRecommendations(
            @Valid @ModelAttribute("styleRequest") StyleRequest styleRequest,
            BindingResult result, Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) { // <--- Add this

        if (result.hasErrors()) return "style/rform";

        // Grab the actual logged-in user's ID!
        Long customerId = userDetails.getCustomerId();

        var recommendations = service.processAndSave(styleRequest, customerId);
        var savedFeature    = service.getLatestFeature(customerId);

        model.addAttribute("recommendations", recommendations);
        model.addAttribute("styleRequest",    styleRequest);
        model.addAttribute("featureId",       savedFeature != null ? savedFeature.getFeatureId() : null);
        return "style/result";
    }

    // GET /style/edit/{featureId} — open edit form pre-filled with saved values
    @GetMapping("/edit/{featureId}")
    public String showEditForm(@PathVariable Long featureId, Model model) {
        CustomerFeature feature = service.getFeatureById(featureId);

        // Map saved CustomerFeature → StyleRequest to pre-fill the form dropdowns
        StyleRequest req = new StyleRequest();
        req.setHairType(feature.getHairType());
        req.setFaceShape(feature.getFaceShape());
        req.setEyeColor(feature.getEyeColor());
        req.setSkinTone(feature.getSkinTone());
        req.setStylePreference(feature.getStylePreference());

        model.addAttribute("styleRequest", req);
        model.addAttribute("featureId",    featureId);
        return "style/rform";
    }

    // POST /style/edit/{featureId} — update feature + regenerate recommendations
    @PostMapping("/edit/{featureId}")
    public String updateRecommendations(
            @PathVariable Long featureId,
            @Valid @ModelAttribute("styleRequest") StyleRequest styleRequest,
            BindingResult result, Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) { // <--- Add this

        if (result.hasErrors()) {
            model.addAttribute("featureId", featureId);
            return "style/rform";
        }

        // Grab the actual logged-in user's ID!
        Long customerId = userDetails.getCustomerId();

        var recommendations = service.updateAndRegenerate(featureId, styleRequest, customerId);

        model.addAttribute("recommendations", recommendations);
        model.addAttribute("styleRequest",    styleRequest);
        model.addAttribute("featureId",       featureId);
        model.addAttribute("edited",          true);
        return "style/result";
    }

    // POST /style/like/{id} — save liked recommendation to Style table
    // POST /style/like/{id} — save liked recommendation to Style table
    @PostMapping("/like/{id}")
    public String likeRecommendation(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) { // Add AuthenticationPrincipal

        Long customerId = userDetails.getCustomerId();
        service.likeRecommendation(id, customerId);
        return "redirect:/style/liked";
    }

    // GET /style/liked — show all saved styles
    @GetMapping("/liked")
    public String showLikedStyles(
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) { // Add AuthenticationPrincipal

        Long customerId = userDetails.getCustomerId();
        model.addAttribute("styles", service.getLikedStyles(customerId));
        return "style/liked";
    }

    // GET /style/delete/{id} — delete a saved style
    @GetMapping("/delete/{id}")
    public String deleteStyle(@PathVariable Long id) {
        service.deleteStyle(id);
        return "redirect:/style/liked";
    }
}
