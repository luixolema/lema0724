package com.lema.test.components.web.api.input;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record CheckoutResponse(
        Long rentalAgreementId,
        String toolCode,
        String toolType,
        String toolBrand,
        Integer rentalDays,
        LocalDate checkOutDate,
        LocalDate dueDate,
        BigDecimal dailyRentalCharge,
        Integer chargeDays,
        BigDecimal preDiscountCharge,
        Integer discountPercent,
        BigDecimal discountAmount,
        BigDecimal finalCharge
) {
    public void printFormattedDetails() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        NumberFormat percentFormatter = NumberFormat.getPercentInstance();


        System.out.println("Id: " + rentalAgreementId);
        System.out.println("Tool code: " + toolCode);
        System.out.println("Tool type: " + toolType);
        System.out.println("Tool brand: " + toolBrand);
        System.out.println("Rental days: " + rentalDays);
        System.out.println("Checkout date: " + checkOutDate.format(dateFormatter));
        System.out.println("Due date: " + dueDate.format(dateFormatter));
        System.out.println("Daily rental charge: " + currencyFormatter.format(dailyRentalCharge.setScale(2, RoundingMode.HALF_UP)));
        System.out.println("Charge days: " + chargeDays);
        System.out.println("Pre-discount charge: " + currencyFormatter.format(preDiscountCharge));
        System.out.println("Discount percent: " + percentFormatter.format(discountPercent / 100.0));
        System.out.println("Discount amount: " + currencyFormatter.format(discountAmount));
        System.out.println("Final charge: " + currencyFormatter.format(finalCharge));
    }
}
