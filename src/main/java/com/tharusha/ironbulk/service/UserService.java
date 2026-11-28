package com.tharusha.ironbulk.service;

import com.tharusha.ironbulk.dto.UpdateTargetsRequest;
import com.tharusha.ironbulk.dto.UserProfileResponse;
import com.tharusha.ironbulk.entity.User;
import com.tharusha.ironbulk.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getCurrentUserProfile(String username) {
        User user = getUserByUsername(username);
        return toResponse(user);
    }

    @Transactional
    public UserProfileResponse updateTargets(String username, UpdateTargetsRequest request) {
        User user = getUserByUsername(username);
        user.setDailyCalorieTarget(request.dailyCalorieTarget());
        user.setDailyProteinTarget(request.dailyProteinTarget());
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    private UserProfileResponse toResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDailyCalorieTarget(),
                user.getDailyProteinTarget(),
                user.getCreatedAt()
        );
    }
}