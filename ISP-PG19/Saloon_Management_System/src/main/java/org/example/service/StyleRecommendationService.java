package org.example.service;

import org.example.domain.CustomerFeature;
import org.example.domain.Recommendation;
import org.example.domain.Style;
import org.example.domain.StyleRequest;
import org.example.repository.CustomerFeatureRepository;
import org.example.repository.RecommendationRepository;
import org.example.repository.StyleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StyleRecommendationService {

    private final CustomerFeatureRepository customerFeatureRepo;
    private final RecommendationRepository  recommendationRepo;
    private final StyleRepository           styleRepo;

    public StyleRecommendationService(CustomerFeatureRepository customerFeatureRepo,
                                      RecommendationRepository  recommendationRepo,
                                      StyleRepository           styleRepo) {
        this.customerFeatureRepo = customerFeatureRepo;
        this.recommendationRepo  = recommendationRepo;
        this.styleRepo           = styleRepo;
    }

    // ── Called when customer submits the form (NEW) ───────────────────
    public List<Recommendation> processAndSave(StyleRequest request, Long customerId) {

        CustomerFeature feature = new CustomerFeature();
        feature.setFaceShape(request.getFaceShape());
        feature.setSkinTone(request.getSkinTone());
        feature.setHairType(request.getHairType());
        feature.setEyeColor(request.getEyeColor());
        feature.setStylePreference(request.getStylePreference());
        feature.setCustomerId(customerId);
        customerFeatureRepo.save(feature);

        List<String> texts = buildRecommendationTexts(request);
        List<Recommendation> saved = new ArrayList<>();
        for (String text : texts) {
            Recommendation rec = new Recommendation();
            rec.setDescription(text);
            rec.setType(request.getStylePreference());
            rec.setCustomerId(customerId);
            saved.add(recommendationRepo.save(rec));
        }
        return saved;
    }

    // ── Get the most recently saved feature for a customer ────────────
    public CustomerFeature getLatestFeature(Long customerId) {
        List<CustomerFeature> features = customerFeatureRepo.findByCustomerId(customerId);
        if (features == null || features.isEmpty()) return null;
        return features.get(features.size() - 1);
    }

    // ── Get a specific feature by ID (for pre-filling edit form) ─────
    public CustomerFeature getFeatureById(Long featureId) {
        return customerFeatureRepo.findById(featureId)
                .orElseThrow(() -> new RuntimeException("Feature not found: " + featureId));
    }

    // ── Update existing feature + delete old recs + regenerate ───────
    public List<Recommendation> updateAndRegenerate(Long featureId,
                                                    StyleRequest request,
                                                    Long customerId) {
        // Update the existing CustomerFeature row
        CustomerFeature feature = customerFeatureRepo.findById(featureId)
                .orElseThrow(() -> new RuntimeException("Feature not found: " + featureId));
        feature.setHairType(request.getHairType());
        feature.setFaceShape(request.getFaceShape());
        feature.setEyeColor(request.getEyeColor());
        feature.setSkinTone(request.getSkinTone());
        feature.setStylePreference(request.getStylePreference());
        customerFeatureRepo.save(feature);

        // Delete old recommendations so fresh ones replace them
        List<Recommendation> old = recommendationRepo.findByCustomerId(customerId);
        recommendationRepo.deleteAll(old);

        // Generate and save new recommendations
        List<String> texts = buildRecommendationTexts(request);
        List<Recommendation> saved = new ArrayList<>();
        for (String text : texts) {
            Recommendation rec = new Recommendation();
            rec.setDescription(text);
            rec.setType(request.getStylePreference());
            rec.setCustomerId(customerId);
            saved.add(recommendationRepo.save(rec));
        }
        return saved;
    }

    // ── Called when customer clicks "♥ Save Style" ────────────────────
    // ── Update the likeRecommendation method ────────────────────
    public void likeRecommendation(Long recommendationId, Long customerId) { // Added customerId parameter
        Recommendation rec = recommendationRepo.findById(recommendationId)
                .orElseThrow(() -> new RuntimeException("Recommendation not found: " + recommendationId));
        Style style = new Style();
        style.setDescription(rec.getDescription());
        style.setType(rec.getType());
        style.setStyleNo("S-" + recommendationId);
        style.setCustomerId(customerId); // Link the style to the customer
        styleRepo.save(style);
    }

    // ── Delete a saved style ──────────────────────────────────────────
    public void deleteStyle(Long styleId) {
        styleRepo.deleteById(styleId);
    }

    // ── Update the getLikedStyles method ─────────────────────────────────
    public List<Style> getLikedStyles(Long customerId) { // Added customerId parameter
        return styleRepo.findByCustomerId(customerId); // Filter by the specific customer
    }

    // ── Recommendation logic ──────────────────────────────────────────
    private List<String> buildRecommendationTexts(StyleRequest request) {
        List<String> recs = new ArrayList<>();

        if (request.getHairType() != null) {
            switch (request.getHairType()) {
                case "straight":
                    recs.add("Try a sleek blowout or flat iron style for a polished look");
                    recs.add("Side-swept bangs work beautifully with straight hair");
                    recs.add("Consider a lob (long bob) cut for easy styling");
                    break;
                case "wavy":
                    recs.add("Enhance your waves with a diffuser and curl-enhancing cream");
                    recs.add("Beach waves with a loose braid overnight is a great natural option");
                    recs.add("A layered cut will bring out your waves beautifully");
                    break;
                case "curly":
                    recs.add("Use the LOC method (Leave-in, Oil, Cream) to define your curls");
                    recs.add("A DevaCut from a curly hair specialist is highly recommended");
                    recs.add("Avoid brushing dry — detangle only with conditioner");
                    break;
                case "coily":
                    recs.add("Deep conditioning weekly will keep coily hair moisturized");
                    recs.add("Protective styles like twists or braids reduce breakage");
                    recs.add("Try the pineapple method at night to preserve your style");
                    break;
            }
        }

        if (request.getFaceShape() != null) {
            switch (request.getFaceShape()) {
                case "oval":
                    recs.add("Almost any hairstyle suits an oval face shape");
                    recs.add("Try a curtain bang to frame your balanced features");
                    break;
                case "round":
                    recs.add("Long layers and side parts help elongate a round face");
                    recs.add("High updos add height and slim the face nicely");
                    break;
                case "square":
                    recs.add("Soft waves and curls soften a strong jawline");
                    recs.add("Side-swept styles and wispy bangs complement a square face");
                    break;
                case "heart":
                    recs.add("Chin-length bobs and lobs balance a wider forehead");
                    recs.add("Add volume at the jaw area to balance heart-shaped features");
                    break;
            }
        }

        if (request.getSkinTone() != null) {
            switch (request.getSkinTone()) {
                case "fair":
                    recs.add("Cool ash or platinum blonde tones complement fair skin beautifully");
                    recs.add("Soft pink or rose gold highlights look stunning on fair skin");
                    break;
                case "medium":
                    recs.add("Warm caramel and honey highlights flatter medium skin tones");
                    recs.add("Copper and auburn tones add a gorgeous glow to medium skin");
                    break;
                case "dark":
                    recs.add("Bold colors like burgundy and deep violet look stunning on dark skin");
                    recs.add("Warm honey highlights create a beautiful contrast on dark skin");
                    break;
                case "olive":
                    recs.add("Golden and warm brown tones harmonize perfectly with olive skin");
                    recs.add("Warm ombre from dark roots to caramel ends suits olive tones");
                    break;
            }
        }

        if (request.getStylePreference() != null) {
            switch (request.getStylePreference()) {
                case "classic":
                    recs.add("A timeless blowout or chignon never goes out of style");
                    recs.add("Structured updos and neat ponytails are always elegant");
                    break;
                case "modern":
                    recs.add("Try a textured lob or an asymmetric cut for a modern edge");
                    recs.add("Sleek straight styles with middle parts are very contemporary");
                    break;
                case "bold":
                    recs.add("Go for a bold color like vivid red, electric blue, or pastel pink");
                    recs.add("Try an avant-garde updo with unexpected texture or volume");
                    break;
                case "natural":
                    recs.add("Embrace your natural texture with minimal heat styling");
                    recs.add("Try protective styles that celebrate your natural pattern");
                    break;
            }
        }

        return recs;
    }
}
