package com.tharusha.ironbulk.repository;

import com.tharusha.ironbulk.entity.BodyWeightLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BodyWeightRepository extends JpaRepository<BodyWeightLog, Long> {
    List<BodyWeightLog> findAllByUserIdOrderByLogDateAsc(Long userId);
    Optional<BodyWeightLog> findByUserIdAndLogDate(Long userId, LocalDate logDate);
    Optional<BodyWeightLog> findByIdAndUserId(Long id, Long userId);
}