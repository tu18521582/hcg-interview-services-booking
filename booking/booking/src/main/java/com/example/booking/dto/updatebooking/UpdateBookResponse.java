package com.example.booking.dto.updatebooking;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateBookResponse {
    Integer resultCode;
    String resultMessage;
    LocalDate newCheckInDate;
    LocalDate newCheckOutDate;
}
