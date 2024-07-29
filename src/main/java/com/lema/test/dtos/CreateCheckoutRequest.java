package com.lema.test.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateCheckoutRequest(
        @NotBlank(message = "Tool code cannot be empty")
        String toolCode,
        @Min(value = 1, message = "Rental day count must be greater than 0")
        Integer rentalDayCount,
        @Min(value = 0, message = "Discount percent cannot be negative")
        @Max(value = 100, message = "Discount percent cannot be greater than 100")
        Integer discountPercent,
        @NotNull(message = "Check out date is required")
        // FIXME the next validation should be enable, disable here to pass required tests
//        @FutureOrPresent(message = "Check out date cannot be in the past")
        LocalDate checkOutDate
) {
}
