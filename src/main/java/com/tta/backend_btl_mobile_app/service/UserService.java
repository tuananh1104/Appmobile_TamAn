package com.tta.backend_btl_mobile_app.service;

import com.tta.backend_btl_mobile_app.dto.request.ChangePasswordRequest;
import com.tta.backend_btl_mobile_app.dto.response.DashboardResponse;
import com.tta.backend_btl_mobile_app.dto.response.UserResponse;
import com.tta.backend_btl_mobile_app.entity.Checkin;
import com.tta.backend_btl_mobile_app.entity.User;
import com.tta.backend_btl_mobile_app.exception.ResourceNotFoundException;
import com.tta.backend_btl_mobile_app.exception.UnauthorizedException;
import com.tta.backend_btl_mobile_app.repository.CheckinRepository;
import com.tta.backend_btl_mobile_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CheckinRepository checkinRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToUserResponse(user);
    }

    public DashboardResponse getUserDashboard(Long userId, int days) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusDays(days);

        // Lấy số check-ins TRONG KHOẢNG THỜI GIAN được filter (7/30/all days)
        long totalCheckIns = checkinRepository.countByUserIdAndCreatedAtBetween(userId, startTime, now);

        // Lấy số check-ins trong tuần này (7 ngày) - dùng cho streak hoặc stats khác
        LocalDateTime weekStart = now.minusDays(7);
        long checkInsThisWeek = checkinRepository.countByUserIdAndCreatedAtBetween(userId, weekStart, now);

        // Lấy checkins trong khoảng thời gian
        List<Checkin> checkins = checkinRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                userId, startTime, now);

        // Tính streak
        int streak = calculateStreak(userId);

        // Tính emotion distribution
        Map<String, Integer> emotionDistribution = calculateEmotionDistribution(checkins);

        // Tính average emotion
        String averageEmotion = calculateAverageEmotion(emotionDistribution);

        // Tính emotion trends
        List<DashboardResponse.EmotionTrend> emotionTrends = calculateEmotionTrends(checkins);

        // Lấy recent checkins
        List<Checkin> recentCheckins = checkinRepository.findRecentByUserId(userId, 5);
        List<DashboardResponse.RecentEntry> recentEntryResponses = recentCheckins.stream()
                .map(c -> new DashboardResponse.RecentEntry(
                        c.getId(),
                        c.getEmotion().name(),
                        c.getCreatedAt(),
                        c.getNote()))
                .collect(Collectors.toList());

        return new DashboardResponse(
                totalCheckIns,
                streak,
                checkInsThisWeek,
                averageEmotion,
                emotionTrends,
                emotionDistribution,
                recentEntryResponses);
    }

    @Transactional
    public User updateThemeMode(Long userId, User.ThemeMode themeMode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setThemeMode(themeMode);
        return userRepository.save(user);
    }

    @Transactional
    public User updateDisplayName(Long userId, String displayName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setDisplayName(displayName);
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Kiểm tra mật khẩu hiện tại có đúng không
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        // Kiểm tra mật khẩu mới và confirm password có khớp không
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getIsActive(),
                user.getThemeMode().name());
    }

    private int calculateStreak(Long userId) {
        List<Checkin> allCheckins = checkinRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if (allCheckins.isEmpty()) {
            return 0;
        }

        int streak = 0;
        LocalDate currentDay = LocalDate.now();

        Set<LocalDate> checkinDays = allCheckins.stream()
                .map(c -> c.getCreatedAt().toLocalDate())
                .collect(Collectors.toSet());

        while (checkinDays.contains(currentDay) || checkinDays.contains(currentDay.minusDays(1))) {
            if (checkinDays.contains(currentDay)) {
                streak++;
            }
            currentDay = currentDay.minusDays(1);
        }

        return streak;
    }

    private Map<String, Integer> calculateEmotionDistribution(List<Checkin> checkins) {
        Map<String, Integer> distribution = new HashMap<>();
        for (Checkin checkin : checkins) {
            String emotion = checkin.getEmotion().name();
            distribution.put(emotion, distribution.getOrDefault(emotion, 0) + 1);
        }
        return distribution;
    }

    private String calculateAverageEmotion(Map<String, Integer> distribution) {
        if (distribution.isEmpty()) {
            return "NEUTRAL";
        }
        return distribution.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("NEUTRAL");
    }

    private List<DashboardResponse.EmotionTrend> calculateEmotionTrends(List<Checkin> checkins) {
        Map<String, List<Checkin>> checkinsByDate = checkins.stream()
                .collect(Collectors.groupingBy(c -> c.getCreatedAt().toLocalDate().toString()));

        List<DashboardResponse.EmotionTrend> trends = new ArrayList<>();
        for (Map.Entry<String, List<Checkin>> entry : checkinsByDate.entrySet()) {
            String date = entry.getKey();
            List<Checkin> dayCheckins = entry.getValue();

            double avgScore = dayCheckins.stream()
                    .mapToDouble(c -> getEmotionScore(c.getEmotion()))
                    .average()
                    .orElse(0.5);

            String dominantEmotion = dayCheckins.stream()
                    .collect(Collectors.groupingBy(Checkin::getEmotion, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(e -> e.getKey().name())
                    .orElse("NEUTRAL");

            trends.add(new DashboardResponse.EmotionTrend(date, avgScore, dominantEmotion));
        }

        trends.sort(Comparator.comparing(DashboardResponse.EmotionTrend::getDate));
        return trends;
    }

    private double getEmotionScore(Checkin.Emotion emotion) {
        return switch (emotion) {
            case HAPPY -> 1.0;
            case JOY -> 0.9;
            case NEUTRAL -> 0.5;
            case SAD -> 0.3;
            case WORRIED -> 0.2;
            case STRESSED -> 0.15;
            case ANGRY -> 0.1;
        };
    }
}
