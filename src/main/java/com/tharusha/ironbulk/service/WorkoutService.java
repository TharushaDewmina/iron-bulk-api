package com.tharusha.ironbulk.service;

import com.tharusha.ironbulk.dto.*;
import com.tharusha.ironbulk.entity.User;
import com.tharusha.ironbulk.entity.Workout;
import com.tharusha.ironbulk.entity.WorkoutSet;
import com.tharusha.ironbulk.repository.UserRepository;
import com.tharusha.ironbulk.repository.WorkoutRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Transactional
    public WorkoutResponse logWorkout(String username, WorkoutRequest request) {
        User user = getUserByUsername(username);

        Workout workout = new Workout();
        workout.setUser(user);
        workout.setSplitType(request.splitType());
        workout.setWorkoutDate(request.workoutDate());
        workout.setNotes(request.notes());

        for (WorkoutSetRequest setReq : request.sets()) {
            WorkoutSet set = new WorkoutSet();
            set.setWorkout(workout);
            set.setExerciseName(setReq.exerciseName());
            set.setSetNumber(setReq.setNumber());
            set.setWeightKg(setReq.weightKg());
            set.setReps(setReq.reps());
            set.setIsDropSet(setReq.isDropSet() != null && setReq.isDropSet());
            workout.getSets().add(set);
        }

        Workout saved = workoutRepository.save(workout);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<WorkoutResponse> getUserWorkouts(String username, LocalDate date) {
        User user = getUserByUsername(username);
        List<Workout> workouts = (date != null)
                ? workoutRepository.findByUserIdAndWorkoutDate(user.getId(), date)
                : workoutRepository.findByUserIdOrderByWorkoutDateDesc(user.getId());

        return workouts.stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public void deleteWorkout(String username, Long workoutId) {
        User user = getUserByUsername(username);
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new IllegalArgumentException("Workout not found"));

        if (!workout.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to delete this workout");
        }
        workoutRepository.delete(workout);
    }

    private WorkoutResponse mapToResponse(Workout workout) {
        List<WorkoutSetResponse> setResponses = workout.getSets().stream()
                .map(s -> new WorkoutSetResponse(
                        s.getId(),
                        s.getExerciseName(),
                        s.getSetNumber(),
                        s.getWeightKg(),
                        s.getReps(),
                        s.getIsDropSet()
                ))
                .toList();

        double totalVolume = workout.getSets().stream()
                .mapToDouble(s -> s.getWeightKg() * s.getReps())
                .sum();

        return new WorkoutResponse(
                workout.getId(),
                workout.getSplitType(),
                workout.getWorkoutDate(),
                workout.getNotes(),
                Math.round(totalVolume * 100.0) / 100.0,
                setResponses
        );
    }
}