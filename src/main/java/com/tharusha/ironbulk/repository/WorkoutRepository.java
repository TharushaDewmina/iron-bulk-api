package com.tharusha.ironbulk.repository;

import com.tharusha.ironbulk.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserIdOrderByWorkoutDateDesc(Long userId);
    List<Workout> findByUserIdAndWorkoutDate(Long userId, LocalDate workoutDate);
}