package com.tta.backend_btl_mobile_app.dto.request;

import com.tta.backend_btl_mobile_app.entity.Checkin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCheckinRequest {

    @NotNull(message = "Emotion is required")
    private Checkin.Emotion emotion;

    @NotNull(message = "Location is required")
    private Checkin.Location locationTag;

    @NotNull(message = "Activity is required")
    private Checkin.Activity activityTag;

    @NotNull(message = "People tag is required")
    private Checkin.People peopleTag;

    private String note;
}

