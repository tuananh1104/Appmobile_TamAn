package com.tta.backend_btl_mobile_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkins", indexes = {
    @Index(name = "idx_checkins_user_created_at", columnList = "user_id, created_at"),
    @Index(name = "idx_checkins_emotion", columnList = "emotion"),
    @Index(name = "idx_checkins_location", columnList = "location_tag"),
    @Index(name = "idx_checkins_activity", columnList = "activity_tag"),
    @Index(name = "idx_checkins_people", columnList = "people_tag")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Checkin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "emotion", nullable = false)
    private Emotion emotion;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_tag", nullable = false)
    private Location locationTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_tag", nullable = false)
    private Activity activityTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "people_tag", nullable = false)
    private People peopleTag;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Emotion {
        HAPPY, JOY, NEUTRAL, SAD, WORRIED, STRESSED, ANGRY
    }

    public enum Location {
        WORK, HOME, COMMUTE, OUTDOOR, OTHER
    }

    public enum Activity {
        MEETING, CODING, STUDY, SOCIAL_MEDIA, EATING, WORKOUT, RELAX, OTHER
    }

    public enum People {
        ALONE, COWORKERS, BOSS, FAMILY, FRIENDS, PARTNER, OTHER
    }
}

