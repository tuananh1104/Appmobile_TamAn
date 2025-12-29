package com.tta.backend_btl_mobile_app.dto.request;

import com.tta.backend_btl_mobile_app.entity.Goal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateGoalRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Target type is required")
    private Goal.TargetType targetType;

    private String targetValue;

    private Integer targetCount = 0;
}
