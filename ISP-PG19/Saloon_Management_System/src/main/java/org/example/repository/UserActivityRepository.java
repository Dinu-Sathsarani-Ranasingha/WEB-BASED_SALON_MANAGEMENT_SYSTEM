package org.example.repository;

import org.example.domain.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    Optional<UserActivity> findByUsername(String username);

    List<UserActivity> findAllByOrderByLastLoginDesc();

    List<UserActivity> findByUserTypeOrderByLastLoginDesc(String userType);

    List<UserActivity> findByUserTypeInOrderByLastLoginDesc(List<String> userTypes);
}