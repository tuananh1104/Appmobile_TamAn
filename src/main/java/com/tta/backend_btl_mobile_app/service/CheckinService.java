package com.tta.backend_btl_mobile_app.service;

import com.tta.backend_btl_mobile_app.dto.request.CreateCheckinRequest;
import com.tta.backend_btl_mobile_app.entity.Checkin;
import com.tta.backend_btl_mobile_app.exception.ResourceNotFoundException;
import com.tta.backend_btl_mobile_app.repository.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckinService {

    private final CheckinRepository checkinRepository;

    @Transactional
    public Checkin createCheckin(Long userId, CreateCheckinRequest request) {
        Checkin checkin = new Checkin();
        checkin.setUserId(userId);
        checkin.setEmotion(request.getEmotion());
        checkin.setLocationTag(request.getLocationTag());
        checkin.setActivityTag(request.getActivityTag());
        checkin.setPeopleTag(request.getPeopleTag());
        checkin.setNote(request.getNote());

        return checkinRepository.save(checkin);
    }

    public List<Checkin> getUserCheckins(Long userId) {
        return checkinRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Checkin> getUserCheckinsInRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return checkinRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            userId, startTime, endTime);
    }

    public Checkin getCheckin(Long checkinId) {
        return checkinRepository.findById(checkinId)
            .orElseThrow(() -> new ResourceNotFoundException("Checkin not found"));
    }

    @Transactional
    public void deleteCheckin(Long userId, Long checkinId) {
        Checkin checkin = getCheckin(checkinId);
        if (!checkin.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to delete this checkin");
        }
        checkinRepository.delete(checkin);
    }

    public long getUserCheckinCount(Long userId) {
        return checkinRepository.countByUserId(userId);
    }

    public long getUserCheckinCountInRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return checkinRepository.countByUserIdAndCreatedAtBetween(userId, startTime, endTime);
    }
}

