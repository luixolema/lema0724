package com.lema.test.services;

import com.lema.test.dtos.CreateCheckoutRequest;
import com.lema.test.entities.RentalAgreement;
import com.lema.test.entities.Tool;
import com.lema.test.repositories.RentalAgreementRepository;
import com.lema.test.repositories.ToolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testUtils.RandomTool;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {
    @Mock()
    private RentalAgreementRepository rentalAgreementRepository;
    @Mock()
    private ToolRepository toolRepository;

    @InjectMocks()
    private CheckoutService checkoutService;

    @Test
    void createCheckout() {
        // given
        Tool tool = RandomTool.randomTool();
        CreateCheckoutRequest request = new CreateCheckoutRequest(
                tool.getToolCode(),
                3,
                10,
                LocalDate.of(2024, 7, 27)
        );

        tool.getToolType().setWeekendCharge(false);
        tool.getToolType().setWeekdayCharge(false);
        tool.getToolType().setHolidayCharge(false);

        ArgumentCaptor<RentalAgreement> argumentCaptor = ArgumentCaptor.forClass(RentalAgreement.class);

        when(toolRepository.findByToolCode(tool.getToolCode())).thenReturn(Optional.of(tool));
        when(rentalAgreementRepository.save(argumentCaptor.capture())).thenReturn(null);

        // When
        checkoutService.createCheckout(request);
        RentalAgreement rental = argumentCaptor.getValue();

        // Then
        assertThat(rental.getToolCode()).isEqualTo(tool.getToolCode());
        assertThat(rental.getToolType()).isEqualTo(tool.getToolType().getName());
        assertThat(rental.getToolBrand()).isEqualTo(tool.getBrand());
        assertThat(rental.getRentalDays()).isEqualTo(request.rentalDayCount());
        assertThat(rental.getCheckOutDate()).isEqualTo(request.checkOutDate());
        assertThat(rental.getDueDate()).isEqualTo(request.checkOutDate().plusDays(request.rentalDayCount() - 1));
        assertThat(rental.getDailyRentalCharge()).isEqualTo(tool.getToolType().getDailyCharge());
        assertThat(rental.getChargeDays()).isEqualTo(0);
        assertThat(rental.getPreDiscountCharge().compareTo(rental.getFinalCharge())).isEqualTo(0);
        assertThat(rental.getPreDiscountCharge().compareTo(rental.getDiscountAmount())).isEqualTo(0);
        assertThat(rental.getDiscountPercent()).isEqualTo(request.discountPercent());
        assertThat(rental.getDiscountAmount().floatValue()).isEqualTo(0);
        assertThat(rental.getFinalCharge().floatValue()).isEqualTo(0);
    }
}