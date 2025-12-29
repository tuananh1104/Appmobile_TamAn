package com.tta.backend_btl_mobile_app.service;

import com.tta.backend_btl_mobile_app.dto.request.CreateGoalRequest;
import com.tta.backend_btl_mobile_app.entity.Goal;
import com.tta.backend_btl_mobile_app.exception.ResourceNotFoundException;
import com.tta.backend_btl_mobile_app.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;

    @Transactional
    public Goal createGoal(Long userId, CreateGoalRequest request) {
        Goal goal = new Goal();
        goal.setUserId(userId);
        goal.setTitle(request.getTitle());
        goal.setDescription(request.getDescription());
        goal.setStartDate(request.getStartDate());
        goal.setEndDate(request.getEndDate());
        goal.setTargetType(request.getTargetType());
        goal.setTargetValue(request.getTargetValue());
        goal.setTargetCount(request.getTargetCount() != null ? request.getTargetCount() : 0);
        goal.setStatus(Goal.Status.ACTIVE);
        goal.setProgressValue(0);

        return goalRepository.save(goal);
    }

    public List<Goal> getUserGoals(Long userId, Goal.Status status) {
        if (status != null) {
            return goalRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status);
        }
        return goalRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Goal getGoal(Long goalId) {
        return goalRepository.findById(goalId)
            .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
    }

    @Transactional
    public Goal saveGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    @Transactional
    public Goal completeGoal(Long userId, Long goalId) {
        Goal goal = getGoal(goalId);
        if (!goal.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to complete this goal");
        }
        goal.setStatus(Goal.Status.COMPLETED);
        return goalRepository.save(goal);
    }

    @Transactional
    public Goal cancelGoal(Long userId, Long goalId) {
        Goal goal = getGoal(goalId);
        if (!goal.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to cancel this goal");
        }
        goal.setStatus(Goal.Status.CANCELLED);
        return goalRepository.save(goal);
    }

    @Transactional
    public Goal updateProgress(Long userId, Long goalId, Integer progressValue) {
        Goal goal = getGoal(goalId);
        if (!goal.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to update this goal");
        }
        goal.setProgressValue(progressValue);
        if (progressValue >= goal.getTargetCount()) {
            goal.setStatus(Goal.Status.COMPLETED);
        }
        return goalRepository.save(goal);
    }

    @Transactional
    public void deleteGoal(Long userId, Long goalId) {
        Goal goal = getGoal(goalId);
        if (!goal.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to delete this goal");
        }
        goalRepository.delete(goal);
    }
}

