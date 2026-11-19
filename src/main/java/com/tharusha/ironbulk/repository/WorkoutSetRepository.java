package com.tharusha.ironbulk.repository;

import com.tharusha.ironbulk.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {
    List<WorkoutSet> findByWorkoutIdOrderBySetNumberAsc(Long workoutId);
}