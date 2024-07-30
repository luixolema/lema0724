package com.lema.test.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class ToolType {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal dailyCharge;

    @Column(nullable = false)
    private Boolean weekdayCharge;

    @Column(nullable = false)
    private Boolean weekendCharge;

    @Column(nullable = false)
    private Boolean holidayCharge;
}
