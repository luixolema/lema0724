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

    @Column
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal dailyCharge;

    @Column
    private Boolean weekdayCharge;

    @Column
    private Boolean weekendCharge;

    @Column
    private Boolean holidayCharge;
}
