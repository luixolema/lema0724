package com.lema.test.controllers;


import com.lema.test.dtos.CreateCheckoutRequest;
import com.lema.test.entities.RentalAgreement;
import com.lema.test.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(URL_CONSTANTS.API_ROOT + "checkout")
@RequiredArgsConstructor
public class CheckOutController {
    private final CheckoutService checkoutService;

    @PostMapping()
    public ResponseEntity<RentalAgreement> registerRental(
            @Valid @RequestBody CreateCheckoutRequest request
    ) {
        RentalAgreement rentalAgreement = this.checkoutService.createCheckout(request);

        // FIXME: not a functional requirement, but used here to be able to see the output (remove it if it is not necessary)
        rentalAgreement.printFormattedDetails();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rentalAgreement);
    }
}
