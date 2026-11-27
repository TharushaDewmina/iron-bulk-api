package com.tharusha.ironbulk.service;

import com.tharusha.ironbulk.dto.BodyWeightRequest;
import com.tharusha.ironbulk.dto.BodyWeightResponse;
import com.tharusha.ironbulk.entity.BodyWeightLog;
import com.tharusha.ironbulk.entity.User;
import com.tharusha.ironbulk.repository.BodyWeightRepository;
import com.tharusha.ironbulk.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BodyWeightService {

    private final BodyWeightRepository bodyWeightRepository;
    private final UserRepository userRepository;

    public BodyWeightService(BodyWeightRepository bodyWeightRepository, UserRepository userRepository) {
        this.bodyWeightRepository = bodyWeightRepository;
        this.userRepository = userRepository;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional
    public BodyWeightResponse upsertWeight(String username, BodyWeightRequest request) {
        User user = getUserByUsername(username);

        BodyWeightLog entry = bodyWeightRepository
                .findByUserIdAndLogDate(user.getId(), request.logDate())
                .orElseGet(() -> {
                    BodyWeightLog newLog = new BodyWeightLog();
                    newLog.setUser(user);
                    newLog.setLogDate(request.logDate());
                    return newLog;
                });

        entry.setWeightKg(request.weightKg());
        entry.setNotes(request.notes() != null && !request.notes().isBlank()
                ? request.notes().trim()
                : null);

        BodyWeightLog saved = bodyWeightRepository.save(entry);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BodyWeightResponse> getWeightHistory(String username) {
        User user = getUserByUsername(username);
        return bodyWeightRepository.findAllByUserIdOrderByLogDateAsc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deleteWeight(String username, Long id) {
        User user = getUserByUsername(username);
        BodyWeightLog entry = bodyWeightRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Weight log not found"));
        bodyWeightRepository.delete(entry);
    }

    private BodyWeightResponse toResponse(BodyWeightLog log) {
        return new BodyWeightResponse(
                log.getId(),
                log.getLogDate(),
                log.getWeightKg(),
                log.getNotes()
        );
    }
}