package com.example.booking.dto.booking;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequest {
    private Long roomTypeId;
    private Long ratePlanId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private List<GuestRequest> guests;
}
