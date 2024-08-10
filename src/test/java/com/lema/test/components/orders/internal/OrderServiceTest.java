package com.lema.test.components.orders.internal;

import com.lema.test.components.tools.api.output.IToolService;
import com.lema.test.components.tools.api.output.ToolRentalData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testUtils.ValueProvider;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock()
    private RentalAgreementRepository rentalAgreementRepository;
    @Mock()
    private IToolService toolService;

    @InjectMocks()
    private OrderService orderService;

    @Test
    @DisplayName("-1 holiday")
    void createRentalAgreement_case1() {

        // given
        ToolRentalData rentalData = ToolRentalData.builder()
                .weekdayCharge(true)
                .weekendCharge(true)
                .holidayCharge(false)
                .dailyCharge(BigDecimal.valueOf(1.99))
                .code(ValueProvider.randomString(""))
                .brand(ValueProvider.randomString(""))
                .toolType(ValueProvider.randomString(""))
                .build();
        int rentalDays = 3;
        int discountPercentage = 10;
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);

        // When
        RentalAgreement rentalAgreement = orderService.createRentalAgreement(
                checkoutDate,
                rentalDays,
                discountPercentage,
                rentalData
        );

        // Then
        assertRentalAgreement(
                rentalAgreement,
                2,
                "3.98",
                "0.40",
                "3.58"
        );
    }


    @Test
    @DisplayName("-2 weekend days (1 holiday also charged) ")
    void createRentalAgreement_case2() {

        // given
        ToolRentalData rentalData = ToolRentalData.builder()
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(true)
                .dailyCharge(BigDecimal.valueOf(1.49))
                .code(ValueProvider.randomString(""))
                .brand(ValueProvider.randomString(""))
                .toolType(ValueProvider.randomString(""))
                .build();
        int rentalDays = 5;
        int discountPercentage = 25;
        LocalDate checkoutDate = LocalDate.of(2015, 7, 2);

        // When
        RentalAgreement rentalAgreement = orderService.createRentalAgreement(
                checkoutDate,
                rentalDays,
                discountPercentage,
                rentalData
        );

        // Then
        assertRentalAgreement(
                rentalAgreement,
                3,
                "4.47",
                "1.12",
                "3.35"
        );
    }

    @Test
    @DisplayName("2 weekdays charged, 1 weekend day free")
    void createRentalAgreement_case3() {

        // given
        ToolRentalData rentalData = ToolRentalData.builder()
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(false)
                .dailyCharge(BigDecimal.valueOf(2.99))
                .code(ValueProvider.randomString(""))
                .brand(ValueProvider.randomString(""))
                .toolType(ValueProvider.randomString(""))
                .build();
        int rentalDays = 3;
        int discountPercentage = 10;
        LocalDate checkoutDate = LocalDate.of(2015, 9, 13);

        // When
        RentalAgreement rentalAgreement = orderService.createRentalAgreement(
                checkoutDate,
                rentalDays,
                discountPercentage,
                rentalData
        );


        // Then
        assertRentalAgreement(
                rentalAgreement,
                2,
                "5.98",
                "0.60",
                "5.38"
        );
    }

    @Test
    @DisplayName("1 weekdays charged, 2 weekend days free")
    void createRentalAgreement_case4() {

        // given
        ToolRentalData rentalData = ToolRentalData.builder()
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(false)
                .dailyCharge(BigDecimal.valueOf(2.99))
                .code(ValueProvider.randomString(""))
                .brand(ValueProvider.randomString(""))
                .toolType(ValueProvider.randomString(""))
                .build();
        int rentalDays = 3;
        int discountPercentage = 10;
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);

        // When
        RentalAgreement rentalAgreement = orderService.createRentalAgreement(
                checkoutDate,
                rentalDays,
                discountPercentage,
                rentalData
        );


        // Then
        assertRentalAgreement(
                rentalAgreement,
                1,
                "2.99",
                "0.30",
                "2.69"
        );
    }


    private void assertRentalAgreement(
            RentalAgreement rentalAgreement,
            int chargeDays,
            String preDiscountCharge,
            String discountAmount,
            String finalCharge
    ) {
        assertThat(rentalAgreement.getChargeDays()).isEqualTo(chargeDays);
        assertThat(rentalAgreement.getPreDiscountCharge()).isEqualByComparingTo(preDiscountCharge);
        assertThat(rentalAgreement.getDiscountAmount()).isEqualByComparingTo(discountAmount);
        assertThat(rentalAgreement.getFinalCharge()).isEqualByComparingTo(finalCharge);
    }
}