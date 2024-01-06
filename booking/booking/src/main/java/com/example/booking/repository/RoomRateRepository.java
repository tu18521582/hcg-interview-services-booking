package com.example.booking.repository;

import com.example.booking.model.entity.RoomRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRateRepository extends JpaRepository<RoomRate, Long> {
    List<RoomRate> findByRatePlanIdAndRoomTypeIdAndDateBetween(Long ratePlanId, Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate);
}
