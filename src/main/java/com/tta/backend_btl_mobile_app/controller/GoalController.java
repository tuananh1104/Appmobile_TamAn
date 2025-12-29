package com.tta.backend_btl_mobile_app.controller;

import com.tta.backend_btl_mobile_app.dto.request.CreateGoalRequest;
import com.tta.backend_btl_mobile_app.dto.response.ApiResponse;
import com.tta.backend_btl_mobile_app.entity.Goal;
import com.tta.backend_btl_mobile_app.security.JwtTokenProvider;
import com.tta.backend_btl_mobile_app.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GoalController {

    private final GoalService goalService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<ApiResponse<Goal>> createGoal(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateGoalRequest request) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        Goal goal = goalService.createGoal(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Goal created successfully", goal));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Goal>>> getUserGoals(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String status) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        Goal.Status goalStatus = status != null ? Goal.Status.valueOf(status.toUpperCase()) : null;
        List<Goal> goals = goalService.getUserGoals(userId, goalStatus);
        return ResponseEntity.ok(ApiResponse.success(goals));
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<ApiResponse<Goal>> getGoal(@PathVariable Long goalId) {
        Goal goal = goalService.getGoal(goalId);
        return ResponseEntity.ok(ApiResponse.success(goal));
    }

    @PutMapping("/{goalId}/complete")
    public ResponseEntity<ApiResponse<Goal>> completeGoal(
            @RequestHeader("Authorization") String token,
            @PathVariable Long goalId) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        Goal goal = goalService.completeGoal(userId, goalId);
        return ResponseEntity.ok(ApiResponse.success("Goal completed", goal));
    }

    @PutMapping("/{goalId}/cancel")
    public ResponseEntity<ApiResponse<Goal>> cancelGoal(
            @RequestHeader("Authorization") String token,
            @PathVariable Long goalId) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        Goal goal = goalService.cancelGoal(userId, goalId);
        return ResponseEntity.ok(ApiResponse.success("Goal cancelled", goal));
    }

    @PatchMapping("/{goalId}/status")
    public ResponseEntity<ApiResponse<Goal>> updateGoalStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long goalId,
            @RequestBody Map<String, String> request) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        String statusStr = request.get("status");
        Goal.Status status = Goal.Status.valueOf(statusStr.toUpperCase());

        Goal goal;
        switch (status) {
            case COMPLETED:
                goal = goalService.completeGoal(userId, goalId);
                break;
            case CANCELLED:
                goal = goalService.cancelGoal(userId, goalId);
                break;
            case ACTIVE:
                // Reactivate goal
                goal = goalService.getGoal(goalId);
                if (!goal.getUserId().equals(userId)) {
                    throw new com.tta.backend_btl_mobile_app.exception.UnauthorizedException("Access denied");
                }
                goal.setStatus(Goal.Status.ACTIVE);
                goal = goalService.saveGoal(goal);
                break;
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }

        return ResponseEntity.ok(ApiResponse.success("Goal status updated", goal));
    }

    @PutMapping("/{goalId}/progress")
    public ResponseEntity<ApiResponse<Goal>> updateProgress(
            @RequestHeader("Authorization") String token,
            @PathVariable Long goalId,
            @RequestBody Map<String, Integer> request) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        Goal goal = goalService.updateProgress(userId, goalId, request.get("progressValue"));
        return ResponseEntity.ok(ApiResponse.success("Progress updated", goal));
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<ApiResponse<Void>> deleteGoal(
            @RequestHeader("Authorization") String token,
            @PathVariable Long goalId) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        goalService.deleteGoal(userId, goalId);
        return ResponseEntity.ok(ApiResponse.success("Goal deleted successfully", null));
    }
}
