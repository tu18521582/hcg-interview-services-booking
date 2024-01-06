package com.example.booking.controller;

import com.example.booking.dto.booking.BookingRequest;
import com.example.booking.dto.updatebooking.UpdateBookResponse;
import com.example.booking.dto.updatebooking.UpdateBookingRequest;
import com.example.booking.exception.CommonException;
import com.example.booking.model.entity.Booking;
import com.example.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
public class BookController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestBody BookingRequest bookingRequest) {
        return bookingService.createBooking(bookingRequest);
    }

    @PutMapping("/{bookingId}/update-dates")
    public ResponseEntity<?> changeCheckInCheckOutDates(@PathVariable Long bookingId,
                                                         @RequestBody UpdateBookingRequest updateBookingRequest) {
        try {
            bookingService.changeCheckInCheckOutDates(bookingId, updateBookingRequest.getCheckInDate(), updateBookingRequest.getCheckOutDate());
            return ResponseEntity.ok().body(UpdateBookResponse.builder()
                            .resultCode(0)
                            .resultMessage("CHANGE BOOKING SUCCESS")
                            .newCheckInDate(updateBookingRequest.getCheckInDate())
                            .newCheckOutDate(updateBookingRequest.getCheckOutDate())
                            .build());
        } catch (CommonException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Change Booking Error");
        }
    }
}
