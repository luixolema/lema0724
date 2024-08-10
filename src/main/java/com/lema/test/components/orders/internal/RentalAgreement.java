package com.lema.test.components.orders.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
