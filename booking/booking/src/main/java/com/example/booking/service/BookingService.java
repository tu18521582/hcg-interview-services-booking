package com.example.booking.service;

import com.example.booking.client.ClientManagement;
import com.example.booking.config.Constant;
import com.example.booking.dto.booking.BookingRequest;
import com.example.booking.dto.booking.GuestRequest;
import com.example.booking.dto.updatebooking.UpdateBookingRequest;
import com.example.booking.exception.CommonException;
import com.example.booking.model.entity.*;
import com.example.booking.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository; // no time to change :(

    @Autowired
    private RatePlanRepository ratePlanResRepository; // no time to change :(

    @Autowired
    private RestTemplate restTemplate;

    public Booking createBooking(BookingRequest bookingRequest) {
        // Validate guest information
        validateGuests(bookingRequest.getGuests());

        // Validate room availability and calc total price if available
        BigDecimal totalPrice = validateRoomAvailabilityAndGetPriceIfAvailable(bookingRequest.getRoomTypeId(), bookingRequest.getRatePlanId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

        // Create booking
        Booking booking = new Booking();
        booking.setBookingDate(LocalDateTime.now());
        booking.setCheckInDate(bookingRequest.getCheckInDate());
        booking.setCheckOutDate(bookingRequest.getCheckOutDate());
        booking.setPriceAtBookingTime(totalPrice);
        booking.setRatePlan(ratePlanResRepository.findById(bookingRequest.getRatePlanId()).orElse(new RatePlan()));
        booking.setRoomType(roomTypeRepository.findById(bookingRequest.getRoomTypeId()).orElse(new RoomType()));

        // Save booking and guests
        booking = bookingRepository.save(booking);
        saveGuests(booking, bookingRequest.getGuests());

        // Update room availability
        updateRoomAvailabilityAfterBooking(bookingRequest.getRoomTypeId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

        return booking;
    }

    public void changeCheckInCheckOutDates(Long bookingId, LocalDate newCheckInDate, LocalDate newCheckOutDate) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new CommonException("Booking id not found"));
        LocalDate oldCheckInDate = booking.getCheckInDate();
        LocalDate oldCheckOutDate = booking.getCheckOutDate();

        // Update booking dates
        booking.setCheckInDate(newCheckInDate);
        booking.setCheckOutDate(newCheckOutDate);
        bookingRepository.save(booking);

        // Update room availability
        updateRoomAvailability(booking.getRoomType().getId(), oldCheckInDate, oldCheckOutDate, newCheckInDate, newCheckOutDate);
    }

    private void validateGuests(List<GuestRequest> guests) {
        for (GuestRequest guestRequest : guests) {
            if (guestRequest.getName() == null || guestRequest.getName().isBlank()) {
                throw new CommonException("Name is required");
            }

            if (!guestRequest.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                throw new CommonException("Email is not valid");
            }
        }
    }

    private BigDecimal validateRoomAvailabilityAndGetPriceIfAvailable(Long roomTypeId, Long ratePlanId, LocalDate checkInDate, LocalDate checkOutDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("roomTypeId", roomTypeId);
        params.put("ratePlanId", ratePlanId);
        params.put("checkInDate", checkInDate);
        params.put("checkOutDate", checkOutDate);
        BigDecimal totalPrice;
        try {
            String totalPriceStr = ClientManagement.getInstance().invokePostMethod(Constant.URL_GET_PRICE, params);
            if (totalPriceStr == null) {
                totalPriceStr = "-1";
            }
            totalPrice = BigDecimal.valueOf(Long.parseLong(totalPriceStr));
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(e.getStatusCode());
        }
        if (totalPrice.equals(BigDecimal.valueOf(-1))) {
            throw new CommonException("room is not valid");
        }

        return totalPrice;
    }

    private void saveGuests(Booking booking, List<GuestRequest> guests) {
        for (GuestRequest guestRequest : guests) {
            Guest guest = new Guest();
            guest.setGuestName(guestRequest.getName());
            guest.setEmail(guestRequest.getEmail());
            guest.setBookings(Set.of(booking));

            guestRepository.save(guest);
        }
    }

    private void updateRoomAvailabilityAfterBooking(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("roomTypeId", roomTypeId);
        params.put("checkInDate", checkInDate);
        params.put("checkOutDate", checkOutDate);
        try {
            ClientManagement.getInstance().invokePutMethod(Constant.URL_UPDATE_ROOM_AVAILABILITY_AFTER_BOOKING, params);
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(e.getStatusCode());
        }
    }

    @Transactional
    public void updateRoomAvailability(Long roomTypeId, LocalDate oldCheckInDate, LocalDate oldCheckOutDate, LocalDate newCheckOutDate, LocalDate newCheckInDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("roomTypeId", roomTypeId);
        params.put("oldCheckInDate", oldCheckInDate);
        params.put("oldCheckOutDate", oldCheckOutDate);
        params.put("newCheckInDate", newCheckOutDate);
        params.put("newCheckOutDate", newCheckInDate);

        try {
            ClientManagement.getInstance().invokePutMethod(Constant.URL_UPDATE_ROOM_AVAILABILITY_WITH_NEW_DATE, params);
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(e.getStatusCode());
        }
    }
}
