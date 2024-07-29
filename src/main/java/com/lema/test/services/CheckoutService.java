package com.lema.test.services;

import com.lema.test.dtos.CreateCheckoutRequest;
import com.lema.test.entities.RentalAgreement;
import com.lema.test.entities.Tool;
import com.lema.test.repositories.RentalAgreementRepository;
import com.lema.test.repositories.ToolRepository;
import com.lema.test.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final ToolRepository toolRepository;
    private final RentalAgreementRepository rentalAgreementRepository;
    private final Logger logger = LoggerFactory.getLogger(CheckoutService.class);


    public RentalAgreement createCheckout(CreateCheckoutRequest request) {
        assert request.discountPercent() <= 100 && request.discountPercent() > 0;
        assert request.rentalDayCount() >= 1;

        Tool tool = toolRepository.findByToolCode(request.toolCode())
                .orElseThrow(() -> {
                    logger.error("Tool not found with code: {}", request.toolCode());
                    return new EntityNotFoundException("Tool not found with code: " + request.toolCode());
                });

        // I assume: the day that the rent start is counted as 1 day, the tool could be returned in the same day, in that case the due date is the same day
        LocalDate dueDate = request.checkOutDate().plusDays(request.rentalDayCount() - 1);

        Integer chargeDays = DateUtils.relevantDaysIn(
                request.checkOutDate(),
                dueDate,
                tool.getToolType().getWeekendCharge(),
                tool.getToolType().getWeekdayCharge(),
                tool.getToolType().getHolidayCharge()
        );

        BigDecimal preDiscountCharge = tool.getToolType().getDailyCharge().multiply(BigDecimal.valueOf(chargeDays));

        BigDecimal discountAmount = preDiscountCharge;

        if (chargeDays > 0 && request.discountPercent() < 100) {
            discountAmount = preDiscountCharge.multiply(BigDecimal.valueOf(request.discountPercent() * 0.01));
        }

        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

        RentalAgreement rentalAgreement = new RentalAgreement();
        rentalAgreement.setToolCode(tool.getToolCode());
        rentalAgreement.setToolType(tool.getToolType().getName());
        rentalAgreement.setToolBrand(tool.getBrand());
        rentalAgreement.setRentalDays(request.rentalDayCount());
        rentalAgreement.setCheckOutDate(request.checkOutDate());
        rentalAgreement.setDueDate(dueDate);
        rentalAgreement.setDailyRentalCharge(tool.getToolType().getDailyCharge());
        rentalAgreement.setChargeDays(chargeDays);
        rentalAgreement.setPreDiscountCharge(preDiscountCharge.setScale(2, RoundingMode.HALF_UP));
        rentalAgreement.setDiscountPercent(request.discountPercent());
        rentalAgreement.setDiscountAmount(discountAmount.setScale(2, RoundingMode.HALF_UP));
        rentalAgreement.setFinalCharge(finalCharge.setScale(2, RoundingMode.HALF_UP));

        return rentalAgreementRepository.save(rentalAgreement);
    }


}
