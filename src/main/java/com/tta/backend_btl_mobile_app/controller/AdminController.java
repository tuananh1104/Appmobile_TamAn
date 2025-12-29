package com.tta.backend_btl_mobile_app.controller;

import com.tta.backend_btl_mobile_app.dto.response.ApiResponse;
import com.tta.backend_btl_mobile_app.entity.User;
import com.tta.backend_btl_mobile_app.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemStats() {
        Map<String, Object> stats = adminService.getSystemStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PatchMapping("/users/{userId}/toggle")
    public ResponseEntity<ApiResponse<User>> toggleUserActive(@PathVariable Long userId) {
        User user = adminService.toggleUserActive(userId);
        return ResponseEntity.ok(ApiResponse.success("User status updated", user));
    }

    @GetMapping("/users/{userId}/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserStats(
            @PathVariable Long userId) {
        Map<String, Object> stats = adminService.getUserStats(userId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
