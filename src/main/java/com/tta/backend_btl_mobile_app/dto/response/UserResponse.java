package com.tta.backend_btl_mobile_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String displayName;
    private String role;
    private LocalDateTime createdAt;
    private Boolean isActive;
    private String themeMode;
}
