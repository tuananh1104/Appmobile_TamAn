package com.tta.backend_btl_mobile_app.service;

import com.tta.backend_btl_mobile_app.dto.request.CreateTipRequest;
import com.tta.backend_btl_mobile_app.entity.Tip;
import com.tta.backend_btl_mobile_app.exception.ResourceNotFoundException;
import com.tta.backend_btl_mobile_app.repository.TipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipService {

    private final TipRepository tipRepository;

    public List<Tip> getActiveTips(String category) {
        if (category != null && !category.isEmpty()) {
            Tip.Category cat = Tip.Category.valueOf(category.toUpperCase());
            return tipRepository.findByCategoryAndIsActiveOrderByCreatedAtDesc(cat, true);
        }
        return tipRepository.findByIsActiveOrderByCreatedAtDesc(true);
    }

    public List<Tip> getAllTips() {
        return tipRepository.findAll();
    }

    public Tip getTip(Long tipId) {
        return tipRepository.findById(tipId)
            .orElseThrow(() -> new ResourceNotFoundException("Tip not found"));
    }

    @Transactional
    public Tip createTip(Long adminId, CreateTipRequest request) {
        Tip tip = new Tip();
        tip.setTitle(request.getTitle());
        tip.setContent(request.getContent());
        tip.setCategory(request.getCategory());
        tip.setCreatedBy(adminId);
        tip.setIsActive(true);

        return tipRepository.save(tip);
    }

    @Transactional
    public Tip updateTip(Long tipId, CreateTipRequest request) {
        Tip tip = getTip(tipId);
        tip.setTitle(request.getTitle());
        tip.setContent(request.getContent());
        tip.setCategory(request.getCategory());

        return tipRepository.save(tip);
    }

    @Transactional
    public void deleteTip(Long tipId) {
        Tip tip = getTip(tipId);
        tipRepository.delete(tip);
    }

    @Transactional
    public Tip toggleTipActive(Long tipId) {
        Tip tip = getTip(tipId);
        tip.setIsActive(!tip.getIsActive());
        return tipRepository.save(tip);
    }
}
