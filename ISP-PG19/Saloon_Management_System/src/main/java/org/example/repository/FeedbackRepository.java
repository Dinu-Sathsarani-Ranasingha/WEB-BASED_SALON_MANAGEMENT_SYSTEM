package org.example.repository;

import org.example.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // Fetches the 3 most recent feedbacks based on their ID
    List<Feedback> findTop3ByOrderByFeedbackIdDesc();
    List<Feedback> findByCustomerId(Long customerId);
}
