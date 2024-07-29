package com.lema.test.controllers.integration;

import com.lema.test.entities.RentalAgreement;
import com.lema.test.repositories.RentalAgreementRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
/**
 * Fixme:
 *  Most likely in the real world should be another profile for testing with a real db.
 *  In that case a data.sql could be used to populate the required data for testing.
 *  (because is a demo) This is commented here to prevent duplication of data:
 *  the migrations already contains the test data and populate the db
 */
//@Sql(
//        scripts = "/test-data.sql",
//        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
//)
class CheckOutControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RentalAgreementRepository rentalAgreementRepository;


    @AfterEach
    void setUp() {
        rentalAgreementRepository.deleteAll();
    }

    @Test
    void registerRental_test1() throws Exception {
        String badRequest = """
                {
                 "toolCode": "JAKR",
                 "rentalDayCount": 5,
                 "discountPercent": 101,
                 "checkOutDate": "2015-09-03"
                }
                """;

        performRequest(badRequest)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subErrors").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subErrors.length()", CoreMatchers.is(1)));
    }

    @Test()
    @DisplayName("-1 holiday")
    void registerRental_test2() throws Exception {
        String request = """
                {
                 "toolCode": "LADW",
                 "rentalDayCount": 3,
                 "discountPercent": 10,
                 "checkOutDate": "2020-07-02"
                }
                """;

        performRequest(request).andExpect(MockMvcResultMatchers.status().isCreated());

        RentalAgreement rentalAgreement = rentalAgreementRepository.findAll().get(0);
        assertRentalAgreement(rentalAgreement, "3.98", "0.40", "3.58");
    }

    @Test
    @DisplayName("-2 weekend days (1 holiday also charged) ")
    void registerRental_test3() throws Exception {
        String request = """
                {
                 "toolCode": "CHNS",
                 "rentalDayCount": 5,
                 "discountPercent": 25,
                 "checkOutDate": "2015-07-02"
                }
                """;

        performRequest(request).andExpect(MockMvcResultMatchers.status().isCreated());

        RentalAgreement rentalAgreement = rentalAgreementRepository.findAll().get(0);
        assertRentalAgreement(rentalAgreement, "4.47", "1.12", "3.35");

    }


    @Test
    @DisplayName("2 weekdays charged, 1 weekend day free")
    void registerRental_test4() throws Exception {
        String request = """
                {
                 "toolCode": "JAKD",
                 "rentalDayCount": 3,
                 "discountPercent": 10,
                 "checkOutDate": "2015-09-03"
                }
                """;

        performRequest(request).andExpect(MockMvcResultMatchers.status().isCreated());

        RentalAgreement rentalAgreement = rentalAgreementRepository.findAll().get(0);
        assertRentalAgreement(rentalAgreement, "5.98", "0.60", "5.38");
    }

    @Test
    @DisplayName("1 weekdays charged, 2 weekend days free")
    void registerRental_test5() throws Exception {
        String request = """
                {
                 "toolCode": "JAKD",
                 "rentalDayCount": 3,
                 "discountPercent": 10,
                 "checkOutDate": "2020-07-02"
                }
                """;

        performRequest(request).andExpect(MockMvcResultMatchers.status().isCreated());

        RentalAgreement rentalAgreement = rentalAgreementRepository.findAll().get(0);
        assertRentalAgreement(rentalAgreement, "2.99", "0.30", "2.69");

    }


    @Test
    void registerRental_throwsNotFoundException() throws Exception {
        String request = """
                {
                 "toolCode": "doesnt_exist",
                 "rentalDayCount": 1,
                 "discountPercent": 10,
                 "checkOutDate": "2025-07-02"
                }
                """;


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    private ResultActions performRequest(String request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        );
    }

    private void assertRentalAgreement(RentalAgreement rentalAgreement, String preDiscount, String discount, String finalPrice) {
        assertThat(rentalAgreement.getPreDiscountCharge()).isEqualByComparingTo(preDiscount);
        assertThat(rentalAgreement.getDiscountAmount()).isEqualByComparingTo(discount);
        assertThat(rentalAgreement.getFinalCharge()).isEqualByComparingTo(finalPrice);
    }
}