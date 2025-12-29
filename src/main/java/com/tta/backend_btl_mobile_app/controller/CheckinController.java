package com.tta.backend_btl_mobile_app.controller;

import com.tta.backend_btl_mobile_app.dto.request.CreateCheckinRequest;
import com.tta.backend_btl_mobile_app.dto.response.ApiResponse;
import com.tta.backend_btl_mobile_app.entity.Checkin;
import com.tta.backend_btl_mobile_app.security.JwtTokenProvider;
import com.tta.backend_btl_mobile_app.service.CheckinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/checkins")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CheckinController {

    private final CheckinService checkinService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<ApiResponse<Checkin>> createCheckin(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateCheckinRequest request) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        Checkin checkin = checkinService.createCheckin(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Checkin created successfully", checkin));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Checkin>>> getUserCheckins(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        List<Checkin> checkins = checkinService.getUserCheckins(userId);
        return ResponseEntity.ok(ApiResponse.success(checkins));
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<Checkin>>> getUserCheckinsInRange(
            @RequestHeader("Authorization") String token,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        List<Checkin> checkins = checkinService.getUserCheckinsInRange(userId, start, end);
        return ResponseEntity.ok(ApiResponse.success(checkins));
    }

    @GetMapping("/{checkinId}")
    public ResponseEntity<ApiResponse<Checkin>> getCheckin(@PathVariable Long checkinId) {
        Checkin checkin = checkinService.getCheckin(checkinId);
        return ResponseEntity.ok(ApiResponse.success(checkin));
    }

    @DeleteMapping("/{checkinId}")
    public ResponseEntity<ApiResponse<Void>> deleteCheckin(
            @RequestHeader("Authorization") String token,
            @PathVariable Long checkinId) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        checkinService.deleteCheckin(userId, checkinId);
        return ResponseEntity.ok(ApiResponse.success("Checkin deleted successfully", null));
    }
}

