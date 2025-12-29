package com.tta.backend_btl_mobile_app.controller;

import com.tta.backend_btl_mobile_app.dto.request.ChangePasswordRequest;
import com.tta.backend_btl_mobile_app.dto.response.ApiResponse;
import com.tta.backend_btl_mobile_app.dto.response.DashboardResponse;
import com.tta.backend_btl_mobile_app.dto.response.UserResponse;
import com.tta.backend_btl_mobile_app.entity.User;
import com.tta.backend_btl_mobile_app.security.JwtTokenProvider;
import com.tta.backend_btl_mobile_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "7") int days) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        DashboardResponse dashboard = userService.getUserDashboard(userId, days);
        return ResponseEntity.ok(ApiResponse.success(dashboard));
    }

    @PutMapping("/theme")
    public ResponseEntity<ApiResponse<User>> updateTheme(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> request) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        User.ThemeMode themeMode = User.ThemeMode.valueOf(request.get("themeMode").toUpperCase());
        User user = userService.updateThemeMode(userId, themeMode);
        return ResponseEntity.ok(ApiResponse.success("Theme updated successfully", user));
    }

    @PutMapping("/display-name")
    public ResponseEntity<ApiResponse<User>> updateDisplayName(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> request) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        User user = userService.updateDisplayName(userId, request.get("displayName"));
        return ResponseEntity.ok(ApiResponse.success("Display name updated successfully", user));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody ChangePasswordRequest request) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        userService.changePassword(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }
}
