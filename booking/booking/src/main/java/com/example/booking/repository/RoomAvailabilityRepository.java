package com.example.booking.repository;

import com.example.booking.model.entity.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {
    List<RoomAvailability> findByRoomTypeIdAndDateBetween(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate);
    Optional<RoomAvailability> findByRoomTypeIdAndDate(Long roomTypeId, LocalDate date);
}