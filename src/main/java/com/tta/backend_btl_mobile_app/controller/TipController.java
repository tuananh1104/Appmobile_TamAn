package com.tta.backend_btl_mobile_app.controller;

import com.tta.backend_btl_mobile_app.dto.request.CreateTipRequest;
import com.tta.backend_btl_mobile_app.dto.response.ApiResponse;
import com.tta.backend_btl_mobile_app.entity.Tip;
import com.tta.backend_btl_mobile_app.security.JwtTokenProvider;
import com.tta.backend_btl_mobile_app.service.TipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tips")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TipController {

    private final TipService tipService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Tip>>> getActiveTips(
            @RequestParam(required = false) String category) {
        List<Tip> tips = tipService.getActiveTips(category);
        return ResponseEntity.ok(ApiResponse.success(tips));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Tip>>> getAllTips() {
        List<Tip> tips = tipService.getAllTips();
        return ResponseEntity.ok(ApiResponse.success(tips));
    }

    @GetMapping("/{tipId}")
    public ResponseEntity<ApiResponse<Tip>> getTip(@PathVariable Long tipId) {
        Tip tip = tipService.getTip(tipId);
        return ResponseEntity.ok(ApiResponse.success(tip));
    }

    // ADMIN ONLY
    @PostMapping
    public ResponseEntity<ApiResponse<Tip>> createTip(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateTipRequest request) {
        Long adminId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        Tip tip = tipService.createTip(adminId, request);
        return ResponseEntity.ok(ApiResponse.success("Tip created successfully", tip));
    }

    @PutMapping("/{tipId}")
    public ResponseEntity<ApiResponse<Tip>> updateTip(
            @PathVariable Long tipId,
            @Valid @RequestBody CreateTipRequest request) {
        Tip tip = tipService.updateTip(tipId, request);
        return ResponseEntity.ok(ApiResponse.success("Tip updated successfully", tip));
    }

    @DeleteMapping("/{tipId}")
    public ResponseEntity<ApiResponse<Void>> deleteTip(@PathVariable Long tipId) {
        tipService.deleteTip(tipId);
        return ResponseEntity.ok(ApiResponse.success("Tip deleted successfully", null));
    }

    @PatchMapping("/{tipId}/toggle")
    public ResponseEntity<ApiResponse<Tip>> toggleTip(@PathVariable Long tipId) {
        Tip tip = tipService.toggleTipActive(tipId);
        return ResponseEntity.ok(ApiResponse.success("Tip status updated", tip));
    }
}
