package com.example.booking.dto.updatebooking;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateBookingRequest {
    Long bookingNumber;
    LocalDate checkInDate;
    LocalDate checkOutDate;
}
