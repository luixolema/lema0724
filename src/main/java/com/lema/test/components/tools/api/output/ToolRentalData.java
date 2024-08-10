package com.lema.test.components.tools.api.output;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ToolRentalData(
        boolean weekendCharge,
        boolean weekdayCharge,
        boolean holidayCharge,
        BigDecimal dailyCharge,
        String code,
        String brand,
        String toolType
) {
}
