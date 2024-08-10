package com.lema.test.components.web.internal.rest;


import com.lema.test.components.web.api.input.CheckoutRequest;
import com.lema.test.components.web.api.input.CheckoutResponse;
import com.lema.test.components.web.api.input.IOrderService;
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
    private final IOrderService orderService;

    @PostMapping()
    public ResponseEntity<CheckoutResponse> registerRental(
            @Valid @RequestBody CheckoutRequest request
    ) {
        CheckoutResponse checkout = this.orderService.generateRentalAgreement(request);
        // FIXME: not a functional requirement, but used here to be able to see the output (remove it if it is not necessary)
        checkout.printFormattedDetails();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(checkout);
    }
}
