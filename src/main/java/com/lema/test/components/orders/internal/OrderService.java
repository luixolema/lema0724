package com.lema.test.components.orders.internal;

import com.lema.test.components.orders.internal.utils.DateUtils;
import com.lema.test.components.tools.api.output.IToolService;
import com.lema.test.components.tools.api.output.ToolRentalData;
import com.lema.test.components.web.api.input.CheckoutRequest;
import com.lema.test.components.web.api.input.CheckoutResponse;
import com.lema.test.components.web.api.input.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
class OrderService implements IOrderService {
    private final IToolService toolService;
    private final RentalAgreementRepository rentalAgreementRepository;


    public CheckoutResponse generateRentalAgreement(CheckoutRequest request) {
        assert request.discountPercent() <= 100 && request.discountPercent() > 0;
        assert request.rentalDayCount() >= 1;

        ToolRentalData toolRentalData = toolService.getToolRentalData(request.toolCode());

        RentalAgreement rentalAgreement = createRentalAgreement(
                request.checkOutDate(),
                request.rentalDayCount(),
                request.discountPercent(),
                toolRentalData
        );
        rentalAgreementRepository.save(rentalAgreement);

        return mapToCheckoutResponse(rentalAgreement);
    }

    RentalAgreement createRentalAgreement(
            LocalDate checkoutDate,
            int rentalDays,
            int discountPercentage,
            ToolRentalData toolRentalData
    ) {
        // I assume: the day that the rent start (checkoutDate) is counted as 1 day
        LocalDate dueDate = checkoutDate.plusDays(rentalDays - 1);

        Integer chargeDays = DateUtils.relevantDaysIn(
                checkoutDate,
                dueDate,
                toolRentalData.weekendCharge(),
                toolRentalData.weekdayCharge(),
                toolRentalData.holidayCharge()
        );

        BigDecimal preDiscountCharge = toolRentalData.dailyCharge().multiply(BigDecimal.valueOf(chargeDays));

        BigDecimal discountAmount = preDiscountCharge;

        if (chargeDays > 0 && discountPercentage < 100) {
            discountAmount = preDiscountCharge.multiply(BigDecimal.valueOf(discountPercentage * 0.01));
        }

        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

        return RentalAgreement.builder()
                .toolCode(toolRentalData.code())
                .toolType(toolRentalData.toolType())
                .toolBrand(toolRentalData.brand())
                .rentalDays(rentalDays)
                .checkOutDate(checkoutDate)
                .dueDate(dueDate)
                .dailyRentalCharge(toolRentalData.dailyCharge())
                .chargeDays(chargeDays)
                .preDiscountCharge(preDiscountCharge.setScale(2, RoundingMode.HALF_UP))
                .discountPercent(discountPercentage)
                .discountAmount(discountAmount.setScale(2, RoundingMode.HALF_UP))
                .finalCharge(finalCharge.setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    private CheckoutResponse mapToCheckoutResponse(RentalAgreement rentalAgreement) {
        return new CheckoutResponse(
                rentalAgreement.getId(),
                rentalAgreement.getToolCode(),
                rentalAgreement.getToolType(),
                rentalAgreement.getToolBrand(),
                rentalAgreement.getRentalDays(),
                rentalAgreement.getCheckOutDate(),
                rentalAgreement.getDueDate(),
                rentalAgreement.getDailyRentalCharge(),
                rentalAgreement.getChargeDays(),
                rentalAgreement.getPreDiscountCharge(),
                rentalAgreement.getDiscountPercent(),
                rentalAgreement.getDiscountAmount(),
                rentalAgreement.getFinalCharge()
        );
    }
}
