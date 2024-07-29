package com.lema.test.controllers;

import com.lema.test.services.CheckoutService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(CheckOutController.class)
class CheckOutControllerTest {

    @MockBean
    CheckoutService checkoutService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerRentalValidationWorks() throws Exception {
        String badRequest = """
                {
                 "toolCode": "",
                 "rentalDayCount": 0,
                 "discountPercent": 120,
                 "checkOutDate": "2024-07-02"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subErrors").isArray())
                // Fixme: enable the first line and remove the 2dn if the @FutureOrPresent validation is added to checkOutDate in CreateCheckoutRequest
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()", CoreMatchers.is(4)));
                .andExpect(MockMvcResultMatchers.jsonPath("$.subErrors.length()", CoreMatchers.is(3)));
    }
}