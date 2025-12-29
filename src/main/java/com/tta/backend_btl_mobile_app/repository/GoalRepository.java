package com.tta.backend_btl_mobile_app.repository;

import com.tta.backend_btl_mobile_app.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Goal> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, Goal.Status status);
}
