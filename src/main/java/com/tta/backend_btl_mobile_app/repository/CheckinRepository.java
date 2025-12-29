package com.tta.backend_btl_mobile_app.repository;

import com.tta.backend_btl_mobile_app.entity.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {

    List<Checkin> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Checkin> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
        Long userId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT COUNT(c) FROM Checkin c WHERE c.userId = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(c) FROM Checkin c WHERE c.userId = :userId " +
           "AND c.createdAt BETWEEN :startTime AND :endTime")
    long countByUserIdAndCreatedAtBetween(
        @Param("userId") Long userId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime);

    @Query("SELECT c.emotion, COUNT(c) FROM Checkin c WHERE c.userId = :userId " +
           "AND c.createdAt >= :since GROUP BY c.emotion")
    List<Object[]> countEmotionsByUserIdSince(
        @Param("userId") Long userId,
        @Param("since") LocalDateTime since);

    @Query(value = "SELECT * FROM checkins WHERE user_id = :userId " +
           "ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<Checkin> findRecentByUserId(@Param("userId") Long userId, @Param("limit") int limit);
}

