package org.example.service;

import org.example.domain.Feedback;
import org.example.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository repo;

    public FeedbackService(FeedbackRepository repo) {
        this.repo = repo;
    }

    public List<Feedback> getAll() {
        return repo.findAll();
    }

    public Feedback getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public void save(Feedback feedback) {
        repo.save(feedback);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<Feedback> getTop3RecentFeedbacks() {
        return repo.findTop3ByOrderByFeedbackIdDesc();
    }

    public List<Feedback> getByCustomerId(Long customerId) {
        return repo.findByCustomerId(customerId);
    }
}
