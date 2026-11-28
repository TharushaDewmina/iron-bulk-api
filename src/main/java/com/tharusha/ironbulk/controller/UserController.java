package com.tharusha.ironbulk.controller;

import com.tharusha.ironbulk.dto.UpdateTargetsRequest;
import com.tharusha.ironbulk.dto.UserProfileResponse;
import com.tharusha.ironbulk.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getCurrentUserProfile(authentication.getName()));
    }

    @PutMapping("/me/targets")
    public ResponseEntity<UserProfileResponse> updateTargets(
            Authentication authentication,
            @Valid @RequestBody UpdateTargetsRequest request) {
        return ResponseEntity.ok(userService.updateTargets(authentication.getName(), request));
    }
}