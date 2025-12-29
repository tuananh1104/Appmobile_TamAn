package com.tta.backend_btl_mobile_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private Long totalCheckIns;
    private Integer streak;
    private Long checkInsThisWeek;
    private String averageEmotion;
    private List<EmotionTrend> emotionTrends;
    private Map<String, Integer> emotionDistribution;
    private List<RecentEntry> recentEntries;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmotionTrend {
        private String date;
        private Double score;
        private String emotion;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentEntry {
        private Long id;
        private String emotion;
        private LocalDateTime createdAt;
        private String note;
    }
}
