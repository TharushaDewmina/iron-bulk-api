package com.tharusha.ironbulk.service;

import com.tharusha.ironbulk.dto.DailyNutritionSummary;
import com.tharusha.ironbulk.dto.MealLogRequest;
import com.tharusha.ironbulk.dto.MealLogResponse;
import com.tharusha.ironbulk.entity.MealLog;
import com.tharusha.ironbulk.entity.User;
import com.tharusha.ironbulk.repository.MealLogRepository;
import com.tharusha.ironbulk.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MealLogService {

    private final MealLogRepository mealLogRepository;
    private final UserRepository userRepository;

    public MealLogService(MealLogRepository mealLogRepository, UserRepository userRepository) {
        this.mealLogRepository = mealLogRepository;
        this.userRepository = userRepository;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Transactional
    public MealLogResponse logMeal(String username, MealLogRequest request) {
        User user = getUserByUsername(username);

        MealLog meal = new MealLog();
        meal.setUser(user);
        meal.setLogDate(request.logDate());
        meal.setFoodName(request.foodName());
        meal.setGrams(request.grams());
        meal.setCalories(request.calories());
        meal.setProteinG(request.proteinG());
        meal.setCarbsG(request.carbsG());
        meal.setFatsG(request.fatsG());

        MealLog saved = mealLogRepository.save(meal);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public DailyNutritionSummary getDailySummary(String username, LocalDate date) {
        User user = getUserByUsername(username);
        LocalDate targetDate = (date != null) ? date : LocalDate.now();

        List<MealLog> meals = mealLogRepository.findByUserIdAndLogDate(user.getId(), targetDate);
        List<MealLogResponse> mealResponses = meals.stream().map(this::mapToResponse).toList();

        double totalCalories = round(meals.stream().mapToDouble(MealLog::getCalories).sum());
        double totalProtein = round(meals.stream().mapToDouble(MealLog::getProteinG).sum());
        double totalCarbs = round(meals.stream().mapToDouble(MealLog::getCarbsG).sum());
        double totalFats = round(meals.stream().mapToDouble(MealLog::getFatsG).sum());

        int targetCalories = user.getDailyCalorieTarget() != null ? user.getDailyCalorieTarget() : 2800;
        double targetProtein = user.getDailyProteinTarget() != null ? user.getDailyProteinTarget() : 110.0;

        return new DailyNutritionSummary(
                targetDate,
                totalCalories,
                targetCalories,
                round(targetCalories - totalCalories),
                totalProtein,
                targetProtein,
                round(targetProtein - totalProtein),
                totalCarbs,
                totalFats,
                mealResponses
        );
    }

    @Transactional
    public void deleteMeal(String username, Long mealId) {
        User user = getUserByUsername(username);
        MealLog meal = mealLogRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal log not found"));

        if (!meal.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to delete this meal log");
        }
        mealLogRepository.delete(meal);
    }

    private MealLogResponse mapToResponse(MealLog m) {
        return new MealLogResponse(
                m.getId(),
                m.getLogDate(),
                m.getFoodName(),
                m.getGrams(),
                m.getCalories(),
                m.getProteinG(),
                m.getCarbsG(),
                m.getFatsG()
        );
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}