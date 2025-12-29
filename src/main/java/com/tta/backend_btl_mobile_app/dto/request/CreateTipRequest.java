package com.tta.backend_btl_mobile_app.dto.request;

import com.tta.backend_btl_mobile_app.entity.Tip;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTipRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Category is required")
    private Tip.Category category;
}

