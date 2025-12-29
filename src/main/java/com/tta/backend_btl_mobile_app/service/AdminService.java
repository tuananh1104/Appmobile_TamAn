package com.tta.backend_btl_mobile_app.service;

import com.tta.backend_btl_mobile_app.entity.Checkin;
import com.tta.backend_btl_mobile_app.entity.User;
import com.tta.backend_btl_mobile_app.exception.ResourceNotFoundException;
import com.tta.backend_btl_mobile_app.repository.CheckinRepository;
import com.tta.backend_btl_mobile_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CheckinRepository checkinRepository;

    public Map<String, Object> getSystemStats() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countActiveUsers();
        long totalCheckIns = checkinRepository.count();

        BigDecimal avgCheckIns = BigDecimal.ZERO;
        if (totalUsers > 0) {
            avgCheckIns = BigDecimal.valueOf(totalCheckIns)
                .divide(BigDecimal.valueOf(totalUsers), 2, RoundingMode.HALF_UP);
        }

        Checkin.Emotion mostCommonEmotion = findMostCommonEmotion();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("activeUsers", activeUsers);
        stats.put("totalCheckIns", totalCheckIns);
        stats.put("averageCheckInsPerUser", avgCheckIns);
        stats.put("mostCommonEmotion", mostCommonEmotion != null ? mostCommonEmotion.name() : "NEUTRAL");

        return stats;
    }

    private Checkin.Emotion findMostCommonEmotion() {
        List<Checkin> allCheckins = checkinRepository.findAll();
        if (allCheckins.isEmpty()) {
            return Checkin.Emotion.NEUTRAL;
        }

        Map<Checkin.Emotion, Long> emotionCount = new HashMap<>();
        for (Checkin checkin : allCheckins) {
            emotionCount.put(checkin.getEmotion(),
                emotionCount.getOrDefault(checkin.getEmotion(), 0L) + 1);
        }

        return emotionCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(Checkin.Emotion.NEUTRAL);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User toggleUserActive(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setIsActive(!user.getIsActive());
        return userRepository.save(user);
    }

    public Map<String, Object> getUserStats(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        long totalCheckIns = checkinRepository.countByUserId(userId);
        List<Checkin> checkins = checkinRepository.findByUserIdOrderByCreatedAtDesc(userId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("userId", userId);
        stats.put("username", user.getUsername());
        stats.put("displayName", user.getDisplayName());
        stats.put("totalCheckIns", totalCheckIns);
        stats.put("isActive", user.getIsActive());
        stats.put("createdAt", user.getCreatedAt());
        stats.put("recentCheckins", checkins.stream().limit(10).toList());

        return stats;
    }
}
