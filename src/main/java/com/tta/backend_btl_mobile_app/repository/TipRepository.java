package com.tta.backend_btl_mobile_app.repository;

import com.tta.backend_btl_mobile_app.entity.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipRepository extends JpaRepository<Tip, Long> {

    List<Tip> findByIsActiveOrderByCreatedAtDesc(Boolean isActive);

    List<Tip> findByCategoryAndIsActiveOrderByCreatedAtDesc(
        Tip.Category category, Boolean isActive);
}
