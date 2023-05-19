package com.appdev.estore.users.usersservice.query;

import com.appdev.estore.core.core.data.FetchUserPaymentDetailsQuery;
import com.appdev.estore.core.core.payment.PaymentDetails;
import com.appdev.estore.core.core.user.User;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserEventsHandler {


    @QueryHandler
    public User returnUserDetails(FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery) {
        log.info("Return user details for user id {}", fetchUserPaymentDetailsQuery.getUserId());

        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("CardNumber")
                .cvv("333")
                .name("ANDREI RAMANOUSKI")
                .validUntilMonth(06)
                .validUntilYear(2025)
                .build();

        User userRest = User.builder()
                .firstName("Andrei")
                .lastName("Ramanouski")
                .userId(fetchUserPaymentDetailsQuery.getUserId())
                .paymentDetails(paymentDetails)
                .build();
        return userRest;
    }
}
