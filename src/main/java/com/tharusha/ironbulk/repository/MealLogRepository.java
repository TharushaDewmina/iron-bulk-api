package com.tharusha.ironbulk.repository;

import com.tharusha.ironbulk.entity.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface MealLogRepository extends JpaRepository<MealLog, Long> {
    List<MealLog> findByUserIdAndLogDate(Long userId, LocalDate logDate);
}