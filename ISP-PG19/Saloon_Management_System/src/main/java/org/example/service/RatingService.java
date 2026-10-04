package org.example.service;

import org.example.domain.Rating;
import org.example.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RatingService {

    private final RatingRepository repo;

    public RatingService(RatingRepository repo) {
        this.repo = repo;
    }

    public List<Rating> getAll() {
        return repo.findAll();
    }

    public Rating getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public void save(Rating rating) {
        rating.setDate(LocalDate.now());
        repo.save(rating);
    }

    @Transactional
    public void update(Long id, Rating data) {
        Rating existing = repo.findById(id).orElseThrow();
        existing.setName(data.getName());
        existing.setRating(data.getRating());
        existing.setService(data.getService());
        existing.setStaffMember(data.getStaffMember());
        existing.setDate(LocalDate.now());
        // No repo.save() needed — @Transactional dirty-check flushes automatically
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<Rating> getByCustomerId(Long customerId) {
        return repo.findByCustomerId(customerId);
    }
}