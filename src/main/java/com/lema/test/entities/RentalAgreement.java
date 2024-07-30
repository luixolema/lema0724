package com.lema.test.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
public class RentalAgreement {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String toolCode;

    @Column(nullable = false)
    private String toolType;

    @Column(nullable = false)
    private String toolBrand;

    @Column(nullable = false)
    private Integer rentalDays;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private BigDecimal dailyRentalCharge;

    @Column(nullable = false)
    private Integer chargeDays;

    @Column(precision = 10, scale = 2)
    private BigDecimal preDiscountCharge;

    @Column(precision = 10, scale = 2)
    private Integer discountPercent;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal finalCharge;

    public void printFormattedDetails() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        NumberFormat percentFormatter = NumberFormat.getPercentInstance();


        System.out.println("Id: " + id);
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
