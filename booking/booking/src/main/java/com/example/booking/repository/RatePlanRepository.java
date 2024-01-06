package com.example.booking.repository;

import com.example.booking.model.entity.RatePlan;
import com.example.booking.model.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatePlanRepository extends JpaRepository<RatePlan, Long> {
    Optional<RatePlan> findById(Long id);
}
