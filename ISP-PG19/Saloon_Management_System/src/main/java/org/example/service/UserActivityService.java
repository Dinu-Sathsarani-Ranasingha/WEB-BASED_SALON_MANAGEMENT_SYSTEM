package org.example.service;

import org.example.domain.UserActivity;
import org.example.repository.UserActivityRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserActivityService {

    private static final Logger logger = LoggerFactory.getLogger(UserActivityService.class);

    private final UserActivityRepository userActivityRepository;

    public UserActivityService(UserActivityRepository userActivityRepository) {
        this.userActivityRepository = userActivityRepository;
    }


    //Track user login, count

    public void trackLogin(String username, String userType, String fullName) {
        try {
            UserActivity activity = userActivityRepository.findByUsername(username)
                    .orElse(new UserActivity(username, userType, fullName));

            activity.setLastLogin(LocalDateTime.now());
            activity.setLoginCount(activity.getLoginCount() + 1);
            activity.setUpdatedAt(LocalDateTime.now());
            activity.setUserType(userType);
            activity.setFullName(fullName);

            userActivityRepository.save(activity);
            logger.info("Login tracked for user: {} ({})", username, userType);
        } catch (Exception e) {
            logger.error("Error tracking login for user: {}", username, e);
        }
    }

    /**
     * Get all user activities sorted by last login
     */
    public List<UserActivity> getAllActivities() {
        return userActivityRepository.findAllByOrderByLastLoginDesc();
    }

    /**
     * Get staff and receptionist activities
     */
    public List<UserActivity> getStaffActivities() {
        return userActivityRepository.findByUserTypeInOrderByLastLoginDesc(
                Arrays.asList("STAFF", "RECEPTIONIST")
        );
    }

    /**
     * Get customer activities
     */
    public List<UserActivity> getCustomerActivities() {
        return userActivityRepository.findByUserTypeOrderByLastLoginDesc("CUSTOMER");
    }


    //Get activities by filter type

    public List<UserActivity> getActivitiesByFilter(String filterType) {
        if ("staff".equalsIgnoreCase(filterType)) {
            return getStaffActivities();
        } else if ("customer".equalsIgnoreCase(filterType)) {
            return getCustomerActivities();
        } else {
            return getAllActivities();
        }
    }

    /**
     * Get activity for specific user
     */
    public UserActivity getUserActivity(String username) {
        return userActivityRepository.findByUsername(username).orElse(null);
    }
}
